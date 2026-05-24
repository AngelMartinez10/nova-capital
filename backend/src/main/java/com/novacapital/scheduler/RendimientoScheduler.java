package com.novacapital.scheduler;

import com.novacapital.entity.Aurus;
import com.novacapital.entity.Inversion;
import com.novacapital.entity.Proyecto;
import com.novacapital.entity.Transaccion;
import com.novacapital.repository.AurusRepository;
import com.novacapital.repository.InversionRepository;
import com.novacapital.repository.ProyectoRepository;
import com.novacapital.repository.TransaccionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * RendimientoScheduler - Paga rendimientos mensuales a los inversores.
 *
 * Se ejecuta el día 1 de cada mes a las 08:00.
 * Solo actúa sobre proyectos en estado FINANCIADO.
 * Para cada inversión calcula: rendimiento = cantidad_invertida * rendimiento_mensual / 100
 * Suma el resultado al saldo Aurus del inversor y registra la Transaccion.
 *
 * Para probar sin esperar un mes, cambia el cron por: "0 * * * * *" (cada minuto)
 */
@Component
public class RendimientoScheduler {

    private final InversionRepository    inversionRepo;
    private final ProyectoRepository     proyectoRepo;
    private final AurusRepository        aurusRepo;
    private final TransaccionRepository  transaccionRepo;

    public RendimientoScheduler(InversionRepository inversionRepo,
                                ProyectoRepository proyectoRepo,
                                AurusRepository aurusRepo,
                                TransaccionRepository transaccionRepo) {
        this.inversionRepo   = inversionRepo;
        this.proyectoRepo    = proyectoRepo;
        this.aurusRepo       = aurusRepo;
        this.transaccionRepo = transaccionRepo;
    }

    @Scheduled(cron = "0 0 8 1 * *")   // Día 1 de cada mes a las 08:00
    @Transactional
    public void pagarRendimientos() {
        List<Proyecto> financiados = proyectoRepo.findByEstado(Proyecto.EstadoProyecto.FINANCIADO);

        for (Proyecto proyecto : financiados) {
            List<Inversion> inversiones = inversionRepo.findByProyectoIdProyecto(proyecto.getIdProyecto());

            for (Inversion inversion : inversiones) {
                BigDecimal rendimiento = inversion.getCantidad()
                        .multiply(proyecto.getRendimientoMensual())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                Aurus aurus = aurusRepo.findByClienteIdCliente(
                                inversion.getCliente().getIdCliente())
                        .orElseThrow();

                BigDecimal saldoAnterior = aurus.getSaldo();
                aurus.setSaldo(saldoAnterior.add(rendimiento));
                aurusRepo.save(aurus);

                Transaccion tx = new Transaccion();
                tx.setCliente(inversion.getCliente());
                tx.setTipo(Transaccion.TipoTransaccion.RECOMPENSA);
                tx.setCantidad(rendimiento);
                tx.setSaldoAnterior(saldoAnterior);
                tx.setSaldoPosterior(aurus.getSaldo());
                tx.setDescripcion("Rendimiento mensual: " + proyecto.getNombre()
                        + " (" + proyecto.getRendimientoMensual() + "% mensual)");
                transaccionRepo.save(tx);
            }
        }
    }
}