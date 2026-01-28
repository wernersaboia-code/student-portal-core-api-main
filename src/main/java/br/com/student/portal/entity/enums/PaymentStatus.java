package br.com.student.portal.entity.enums;

public enum PaymentStatus {
    PENDENTE,
    PAGO,
    ATRASADO,
    CANCELADO;

    @Override
    public String toString() {
        return name().toUpperCase();
    }
}
