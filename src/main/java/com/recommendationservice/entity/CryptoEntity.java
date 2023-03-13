package com.recommendationservice.entity;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "crypto_values")
public class CryptoEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "price_date")
    private LocalDateTime priceDate;

    @Column(name = "symbol")
    private String symbol;
    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "uploadedFileId")
    private UploadedFileEntity uploadedFileEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CryptoEntity that = (CryptoEntity) o;

        return new EqualsBuilder().append(id, that.id).append(priceDate, that.priceDate).append(symbol, that.symbol).append(price, that.price).append(uploadedFileEntity, that.uploadedFileEntity).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(priceDate).append(symbol).append(price).append(uploadedFileEntity).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("priceDate", priceDate)
                .append("symbol", symbol)
                .append("price", price)
                .append("uploadedFileEntity", uploadedFileEntity)
                .toString();
    }

    public BigDecimal setPriceForNormalized(BigDecimal price) {
        this.price = price;
        return this.price;
    }
}


