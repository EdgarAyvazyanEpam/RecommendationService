package com.recommendationservice.entity;

import com.recommendationservice.enums.UploadedFIleStatusEnum;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "uploaded_file", indexes = @Index(columnList = "file_name, file_status"))
public class UploadedFileEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 6209680594148781021L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "file_name")
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_status")
    private UploadedFIleStatusEnum fileStatus;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "uploadedFileEntity")
    private Set<CryptoEntity> cryptoEntity = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UploadedFileEntity that = (UploadedFileEntity) o;

        return new EqualsBuilder().append(id, that.id).append(fileName, that.fileName).append(fileStatus, that.fileStatus).append(creationDate, that.creationDate).append(cryptoEntity, that.cryptoEntity).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(fileName).append(fileStatus).append(creationDate).append(cryptoEntity).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("fileName", fileName)
                .append("fileStatus", fileStatus)
                .append("creationDate", creationDate)
                .append("cryptoEntity", cryptoEntity)
                .toString();
    }
}
