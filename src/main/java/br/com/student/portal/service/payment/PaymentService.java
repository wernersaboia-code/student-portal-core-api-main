package br.com.student.portal.service.payment;

import br.com.student.portal.entity.Payment;
import br.com.student.portal.repository.PaymentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public List<Payment> getPaymentsByStudentId(Long studentId) {
        return paymentRepository.findByStudentId(studentId);
    }

    public Payment createPayment(Payment payment) {
        payment.setPaymentDate(LocalDate.now());
        payment.setStatus("PAGO");
        return savePayment(payment);
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void updateOverduePayments() {
        paymentRepository.findByDueDateBeforeAndStatus(LocalDate.now(), "PENDENTE")
                .forEach(payment -> {
                    payment.setStatus("ATRASADO");
                    savePayment(payment);
                });
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    private Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
}