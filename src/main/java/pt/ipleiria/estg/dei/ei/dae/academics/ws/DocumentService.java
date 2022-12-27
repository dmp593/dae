package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.core.MultivaluedMap;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.DocumentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.DocumentBean;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.StudentBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Authenticated;

@Path("documents")
@Authenticated
@RolesAllowed({"Student", "Administrator"})
public class DocumentService {
    @EJB
    private StudentBean studentBean;

    @EJB
    private DocumentBean documentBean;

    @Context
    private SecurityContext securityContext;

    @POST
    @Path("")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(MultipartFormDataInput input) throws IOException {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        var username = securityContext.getUserPrincipal().getName();

        List<InputPart> inputParts = uploadForm.get("file");
        var documents = new LinkedList<Document>();

        for (InputPart inputPart : inputParts) {
            MultivaluedMap<String, String> headers = inputPart.getHeaders();
            String filename = getFilename(headers);

            // convert the uploaded file to inputstream
            InputStream inputStream = inputPart.getBody(InputStream.class, null);

            byte[] bytes = IOUtils.toByteArray(inputStream);

            String homedir = System.getProperty("user.home");
            String dirpath = homedir + File.separator + "uploads" + File.separator + username;
            mkdirIfNotExists(dirpath);

            String filepath =  dirpath + File.separator + filename;
            writeFile(bytes, filepath);

            var document = documentBean.create(filepath, filename, username);
            documents.add(document);
        }

        return Response.ok(DocumentDTO.from(documents)).build();
    }

    private void mkdirIfNotExists(String path) {
        File file = new File(path);

        if (! file.exists()) {
            file.mkdirs();
        }
    }

    @GET
    @Path("download/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("id") Long id) {
        var document = documentBean.findOrFail(id);
        var response = Response.ok(new File(document.getFilepath()));
        
        response.header("Content-Disposition", "attachment;filename=" + document.getFilename());

        return response.build();
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDocuments() {
        var username = securityContext.getUserPrincipal().getName();
        var documents = documentBean.getStudentDocuments(username);
        return Response.ok(DocumentDTO.from(documents)).build();
    }

    @GET
    @Path("/exists")
    public Response hasDocuments() {
        var username = securityContext.getUserPrincipal().getName();
        var student = studentBean.findOrFail(username);

        return Response.status(Response.Status.OK).entity(! student.getDocuments().isEmpty()).build();
    }

    private String getFilename(MultivaluedMap<String, String> headers) {
        String[] contentDisposition = headers.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                return name[1].trim().replaceAll("\"", "");
            }
        }

        return "unknown";
    }

    private void writeFile(byte[] content, String filename) throws IOException {
        var file = new File(filename);

        if (!file.exists()) {
            file.createNewFile();
        }
        
        FileOutputStream fop = new FileOutputStream(file);

        fop.write(content);
        fop.flush();
        fop.close();

        System.out.println("Written: " + filename);
    }
}
