package pt.ipleiria.estg.dei.ei.dae.academics.dtos;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PaginatedDTO<DTO> implements Serializable {

    public class MetadataDTO implements Serializable {

        public final Long count;
        public final Long totalCount;

        public MetadataDTO(Long count, Long totalCount) {
            this.count = count;
            this.totalCount = totalCount;
        }

        public MetadataDTO(Long totalCount) {
            this(0L, totalCount);
        }
    }

    public final List<DTO> data;

    public final MetadataDTO metadata;

    public PaginatedDTO(long totalCount) {
        this.data = Collections.emptyList();
        this.metadata = new MetadataDTO(0L, totalCount);
    }

    public PaginatedDTO(List<DTO> data, long totalCount, int offset) {
        this.data = data;
        this.metadata = data.isEmpty() ? new MetadataDTO(0L, totalCount) : new MetadataDTO((long) (offset + data.size()), totalCount);
    }
}
