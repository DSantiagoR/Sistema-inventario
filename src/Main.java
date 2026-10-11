import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Bodega bodega = new Bodega("B-01", "Bodega Central");

        Estanteria estA = new Estanteria("Estantería A");
        Cajon cajA1 = new Cajon("Cajón A1");
        Ubicacion a1n1 = new Ubicacion("A", "A1", "1");
        Ubicacion a1n2 = new Ubicacion("A", "A1", "2");
        cajA1.agregar(a1n1);
        cajA1.agregar(a1n2);
        Cajon cajA2 = new Cajon("Cajón A2");
        Ubicacion a2n1 = new Ubicacion("A", "A2", "1");
        cajA2.agregar(a2n1);
        estA.agregar(cajA1);
        estA.agregar(cajA2);

        Estanteria estB = new Estanteria("Estantería B");
        Cajon cajB1 = new Cajon("Cajón B1");
        Ubicacion b1n1 = new Ubicacion("B", "B1", "1");
        Ubicacion b1n2 = new Ubicacion("B", "B1", "2");
        cajB1.agregar(b1n1);
        cajB1.agregar(b1n2);
        estB.agregar(cajB1);

        bodega.agregar(estA);
        bodega.agregar(estB);

        Medicamento amoxicilina = new Medicamento("MED-001", "Amoxicilina 250mg", 20, 30);
        Medicamento acetaminofen = new Medicamento("MED-002", "Acetaminofén 500mg", 15, 30);
        Medicamento ibuprofeno = new Medicamento("MED-003", "Ibuprofeno 400mg", 10, 30);

        LocalDate hoy = LocalDate.now();
        Lote l1 = new Lote("L-001", "AMX-2026-01", hoy.minusDays(5), hoy.plusDays(180), 60, amoxicilina);
        Lote l2 = new Lote("L-002", "ACE-2026-07", hoy.minusDays(20), hoy.plusDays(12), 80, acetaminofen);
        Lote l3 = new Lote("L-003", "IBU-2026-03", hoy.minusDays(10), hoy.plusDays(25), 40, ibuprofeno);

        a1n1.asignarLote(l1);
        a2n1.asignarLote(l2);
        b1n1.asignarLote(l3);

        System.out.println("=== 1. Estructura completa de la bodega (recorrido recursivo) ===");
        bodega.mostrarEstructura(0);

        System.out.println();
        System.out.println("=== 2. Mismo método en distintos niveles del árbol ===");
        imprimirResumen(bodega);
        imprimirResumen(estA);
        imprimirResumen(cajA1);
        imprimirResumen(a1n1);
        imprimirResumen(a1n2);

        System.out.println();
        System.out.println("=== 3. Bodega.listarUbicacionesLibres() (RF-007) ===");
        for (Ubicacion u : bodega.listarUbicacionesLibres()) {
            System.out.println("  - " + u.getNombre());
        }

        System.out.println();
        System.out.println("=== 4. Intento de asignar un lote a una ubicación ocupada (RF-008) ===");
        Lote l4 = new Lote("L-004", "AMX-2026-02", hoy, hoy.plusDays(200), 30, amoxicilina);
        try {
            a1n1.asignarLote(l4);
        } catch (IllegalStateException e) {
            System.out.println("Rechazado: " + e.getMessage());
            Ubicacion sugerida = bodega.listarUbicacionesLibres().get(0);
            System.out.println("Ubicación libre sugerida: " + sugerida.getNombre());
            sugerida.asignarLote(l4);
            System.out.println("Lote " + l4.getIdLote() + " asignado a la ubicación sugerida.");
        }
        imprimirResumen(bodega);

        System.out.println();
        System.out.println("=== 5. Lotes que vencen en 30 días o menos (RF-016) ===");
        imprimirLotes("Toda la bodega:", bodega.buscarLotesPorVencer(30));
        imprimirLotes("Solo Estantería A:", estA.buscarLotesPorVencer(30));
        imprimirLotes("Solo Estantería B:", estB.buscarLotesPorVencer(30));

        System.out.println();
        System.out.println("=== 6. Reasignación del lote L-002 de Est. A / Cajón A2 a Est. B / Cajón B1 (RF-006) ===");
        a2n1.liberar();
        b1n2.asignarLote(l2);
        imprimirResumen(estA);
        imprimirResumen(estB);
        imprimirResumen(bodega);

        System.out.println();
        System.out.println("=== 7. Intentar agregar un hijo a una ubicación (hoja) ===");
        try {
            a1n2.agregar(new Ubicacion("A", "A1", "3"));
        } catch (UnsupportedOperationException e) {
            System.out.println("Excepción esperada: " + e.getMessage());
        }
    }

    private static void imprimirResumen(ElementoAlmacenamiento e) {
        System.out.printf("%-30s -> %d/%d ubicaciones ocupadas, %3d unidades%n",
                e.getNombre(), e.contarUbicacionesOcupadas(), e.contarUbicaciones(), e.getUnidadesAlmacenadas());
    }

    private static void imprimirLotes(String titulo, List<Lote> lotes) {
        System.out.println(titulo);
        if (lotes.isEmpty()) {
            System.out.println("  (ninguno)");
        }
        for (Lote l : lotes) {
            System.out.printf("  - %s (%s): vence en %d días%n",
                    l.getIdLote(), l.getMedicamento().getNombre(), l.calcularDiasParaVencer());
        }
    }
}
