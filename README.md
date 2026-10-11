# Patrón Composite — Sistema de Gestión de Inventario Farmacéutico

Implementación en **Java** del patrón de diseño **Composite** aplicada a la estructura física de la bodega de un centro de distribución farmacéutica.

**Equipo 3** Diseño de Sistemas de Información, Semestre 6, Instituto Tecnológico Metropolitano (ITM), Medellín 2026
Aili Yeraldi Parra Echeverri · David Santiago Rendon · Samuel Yepes Zuleta

---

## 1. ¿Qué es el patrón Composite?

Composite es un patrón estructural que permite organizar objetos en forma de **árbol** (jerarquías parte-todo) y tratar de la **misma manera** a un objeto individual (una *hoja*) y a un grupo de objetos (un *contenedor*). El código que usa el árbol llama a un mismo método sin preguntar si está frente a un contenedor o a una hoja.

| Rol del patrón | Clase en este proyecto |
|---|---|
| **Component** (interfaz común) | `ElementoAlmacenamiento` |
| **Composite** (nodo con hijos) | `Contenedor` (abstracta), de la que heredan `Bodega`, `Estanteria` y `Cajon` |
| **Leaf** (hoja, sin hijos) | `Ubicacion` |

## 2. ¿Dónde se aplica en el proyecto?

En el módulo de **ubicación de medicamentos en bodega** (caso de uso CU-002, requisitos RF-005 a RF-008). Los medicamentos se guardan en una jerarquía física:

```
Bodega
 └── Estantería
      └── Cajón
           └── Ubicación (nivel)  ← aquí se guarda un Lote
```

En el diagrama de clases de negocio, `Bodega` contiene directamente a `Ubicacion`, y `Ubicacion` tiene los atributos `estanteria`, `cajon` y `nivel`. Para poder aplicar Composite, esa jerarquía implícita se convirtió en un **árbol real de objetos**: `Estanteria` y `Cajon` son nodos intermedios, y `Ubicacion` conserva sus atributos como en el diagrama original.

## 3. ¿Cómo se aplica? (el punto central del patrón)

Todas las consultas se escriben **una sola vez** y funcionan en cualquier nivel del árbol:

- En una **hoja** (`Ubicacion`), la operación responde directamente con su propio estado.
- En un **contenedor** (`Bodega`, `Estanteria`, `Cajon`), la operación se **delega recursivamente** en cada hijo y combina los resultados.

Por ejemplo, `getUnidadesAlmacenadas()`:

```java
// Hoja (Ubicacion): devuelve la cantidad de su lote, o 0 si está libre
public int getUnidadesAlmacenadas() {
    return lote == null ? 0 : lote.getCantidad();
}

// Contenedor: suma lo que respondan sus hijos (sean contenedores u hojas)
public int getUnidadesAlmacenadas() {
    int total = 0;
    for (ElementoAlmacenamiento hijo : hijos) {
        total += hijo.getUnidadesAlmacenadas();
    }
    return total;
}
```

Gracias a esto, el cliente puede hacer:

```java
bodega.getUnidadesAlmacenadas();     // total de toda la bodega
estanteriaA.getUnidadesAlmacenadas(); // total de una estantería
cajonA1.getUnidadesAlmacenadas();     // total de un cajón
ubicacion.getUnidadesAlmacenadas();   // una ubicación puntual
```

Los totales **no se guardan** en ningún lado: se calculan recorriendo el árbol cada vez. Por eso, si se reasigna un lote, todos los niveles quedan actualizados automáticamente.


## 4. ¿Qué hace cada clase?

### Clases del patrón Composite

| Clase | Rol | Qué hace |
|---|---|---|
| `ElementoAlmacenamiento` | Component | Interfaz común a todo el árbol. Declara las consultas (`contarUbicaciones`, `contarUbicacionesOcupadas`, `getUnidadesAlmacenadas`, `listarUbicacionesLibres`, `buscarLotesPorVencer`, `mostrarEstructura`). Incluye `agregar()` y `eliminar()` con una implementación por defecto que lanza `UnsupportedOperationException`, porque una hoja no puede tener hijos. |
| `Contenedor` | Composite (abstracta) | Guarda la lista de `hijos` y implementa cada consulta delegando recursivamente en ellos. Implementa `agregar()` y `eliminar()`. |
| `Bodega` | Composite (raíz) | Raíz del árbol. Equivale a la clase `Bodega` del diagrama de clases (`idBodega`, `nombre`). Su método `listarUbicacionesLibres()` es el del diagrama y se resuelve por el recorrido recursivo. |
| `Estanteria` | Composite (intermedio) | Agrupa varios cajones. No agrega lógica propia: solo ocupa su lugar en el árbol. |
| `Cajon` | Composite (intermedio) | Agrupa varios niveles (ubicaciones). Igual que `Estanteria`, no duplica lógica. |
| `Ubicacion` | Leaf (hoja) | Punto físico final (estantería, cajón y nivel) donde se guarda como máximo **un** lote. Permite `asignarLote()`, `liberar()` y `estaOcupada()`. Rechaza asignar un lote a una ubicación ya ocupada (RF-008). No puede tener hijos. |

### Clases de soporte (modelo de negocio)

| Clase | Qué hace |
|---|---|
| `Lote` | Lote de un medicamento: identificador, número, fechas de ingreso y vencimiento, cantidad y medicamento asociado. `calcularDiasParaVencer()` devuelve los días que faltan para que venza; `actualizarCantidad()` modifica la cantidad. |
| `Medicamento` | Medicamento del catálogo: código, nombre, umbral mínimo de stock y días de alerta de vencimiento. |
| `EstadoUbicacion` | Enumeración con los estados `DISPONIBLE` y `OCUPADA`. |
| `Main` | Programa de demostración que construye el árbol y ejecuta los casos que muestran el patrón en funcionamiento. |

## 5. Operaciones del patrón y requisitos que cubren

| Operación | Qué hace | Requisito |
|---|---|---|
| `contarUbicaciones()` / `contarUbicacionesOcupadas()` | Cuenta ubicaciones totales y ocupadas del subárbol | RF-007 |
| `getUnidadesAlmacenadas()` | Suma las unidades de los lotes almacenados en el subárbol | RF-012, RF-013 |
| `listarUbicacionesLibres()` | Lista las ubicaciones disponibles del subárbol | RF-007 |
| `Ubicacion.asignarLote()` | Asigna un lote a una ubicación y rechaza si ya está ocupada | RF-005, RF-008 |
| `Ubicacion.liberar()` + `asignarLote()` | Reasigna un lote a otra ubicación | RF-006 |
| `buscarLotesPorVencer(dias)` | Encuentra los lotes que vencen en el plazo indicado dentro del subárbol | RF-016 |

## 6. Qué muestra la demostración (`Main`)

Al ejecutarlo se construye este árbol:

```
Bodega Central
 ├── Estantería A
 │    ├── Cajón A1  → Nivel 1, Nivel 2
 │    └── Cajón A2  → Nivel 1
 └── Estantería B
      └── Cajón B1  → Nivel 1, Nivel 2
```

y se ejecutan siete pasos:

1. **Estructura completa:** imprime el árbol con un recorrido recursivo.
2. **Mismo método en distintos niveles:** la misma consulta sobre la bodega, una estantería, un cajón y una ubicación.
3. **`listarUbicacionesLibres()`** sobre toda la bodega.
4. **Ubicación ocupada:** se intenta asignar un lote a una ubicación ocupada, el sistema rechaza y sugiere una libre.
5. **Lotes por vencer:** búsqueda de lotes que vencen en 30 días o menos, por bodega y por cada estantería.
6. **Reasignación:** se mueve un lote de una estantería a otra y los totales de cada nivel se actualizan solos.
7. **Excepción:** al intentar agregar un hijo a una `Ubicacion` se lanza `UnsupportedOperationException`.

Fragmento de la salida:

```
=== 2. Mismo método en distintos niveles del árbol ===
Bodega Central                 -> 3/5 ubicaciones ocupadas, 180 unidades
Estantería A                   -> 2/3 ubicaciones ocupadas, 140 unidades
Cajón A1                       -> 1/2 ubicaciones ocupadas,  60 unidades
Est. A / Cajón A1 / Nivel 1    -> 1/1 ubicaciones ocupadas,  60 unidades
Est. A / Cajón A1 / Nivel 2    -> 0/1 ubicaciones ocupadas,   0 unidades
```

## 7. Decisiones de diseño

- **Transparencia sobre seguridad:** `agregar()` y `eliminar()` están en la interfaz para que todo el árbol se trate igual. La hoja no los soporta y lanza una excepción si se intentan usar.
- **`Estanteria` y `Cajon` casi vacías:** se comportan igual que cualquier contenedor, por eso heredan de `Contenedor` en lugar de duplicar lógica. Existen para reflejar la jerarquía física y hacer legible el árbol.
- **Sin totales almacenados:** capacidad, conteos y unidades se calculan al consultar, así que nunca quedan desactualizados tras un cambio.
- **Datos en memoria:** la demostración no usa base de datos, porque el objetivo es mostrar el patrón.
