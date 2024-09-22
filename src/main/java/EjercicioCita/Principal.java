package EjercicioCita;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class Principal {
    private static List<Paciente> pacientes = new ArrayList<>();
    private static List<Cita> citas = new ArrayList<>();
    private static final String PACIENTES_FILE = "pacientes.txt";
    private static final String CITAS_FILE = "citas.txt";

    public static void main(String[] args) {
        cargarDatos();
        int opcion;

        do {
            String[] opciones = {
                    "Registrar un nuevo paciente",
                    "Actualizar datos del paciente",
                    "Registrar una nueva cita para un paciente existente",
                    "Eliminar una cita asignada",
                    "Mostrar lista de pacientes registrados",
                    "Mostrar lista de citas de un paciente",
                    "Salir"
            };

            opcion = JOptionPane.showOptionDialog(null, "Seleccione una opción", "Menú Principal",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);

            switch (opcion) {
                case 0:
                    registrarPaciente();
                    break;
                case 1:
                    actualizarPaciente();
                    break;
                case 2:
                    registrarCita();
                    break;
                case 3:
                    eliminarCita();
                    break;
                case 4:
                    mostrarPacientes();
                    break;
                case 5:
                    mostrarCitasPaciente();
                    break;
                case 6:
                    guardarDatos();
                    JOptionPane.showMessageDialog(null, "Saliendo del programa");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
            }
        } while (opcion != 6);
    }

    private static void registrarPaciente() {
        String nombre = JOptionPane.showInputDialog("Nombre:");
        String apellido = JOptionPane.showInputDialog("Apellido:");
        int edad = Integer.parseInt(JOptionPane.showInputDialog("Edad:"));
        String genero = JOptionPane.showInputDialog("Género:");
        String direccion = JOptionPane.showInputDialog("Dirección:");
        String telefono = JOptionPane.showInputDialog("Teléfono:");

        Paciente paciente = new Paciente(nombre, apellido, edad, genero, direccion, telefono);
        pacientes.add(paciente);
        JOptionPane.showMessageDialog(null, "Paciente registrado exitosamente.");
    }

    private static void actualizarPaciente() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del paciente a actualizar:");
        Paciente paciente = buscarPaciente(nombre);
        if (paciente != null) {
            String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre (dejar en blanco para mantener):", paciente.getNombre());
            if (!nuevoNombre.isEmpty()) {
                paciente.setNombre(nuevoNombre);
            }
            String nuevoApellido = JOptionPane.showInputDialog("Nuevo apellido (dejar en blanco para mantener):", paciente.getApellido());
            if (!nuevoApellido.isEmpty()) {
                paciente.setApellido(nuevoApellido);
            }
            String edadStr = JOptionPane.showInputDialog("Nueva edad (dejar en blanco para mantener):", paciente.getEdad());
            if (!edadStr.isEmpty()) {
                paciente.setEdad(Integer.parseInt(edadStr));
            }
            String nuevoGenero = JOptionPane.showInputDialog("Nuevo género (dejar en blanco para mantener):", paciente.getGenero());
            if (!nuevoGenero.isEmpty()) {
                paciente.setGenero(nuevoGenero);
            }
            String nuevaDireccion = JOptionPane.showInputDialog("Nueva dirección (dejar en blanco para mantener):", paciente.getDireccion());
            if (!nuevaDireccion.isEmpty()) {
                paciente.setDireccion(nuevaDireccion);
            }
            String nuevoTelefono = JOptionPane.showInputDialog("Nuevo teléfono (dejar en blanco para mantener):", paciente.getTelefono());
            if (!nuevoTelefono.isEmpty()) {
                paciente.setTelefono(nuevoTelefono);
            }
            JOptionPane.showMessageDialog(null, "Datos del paciente actualizados.");
        } else {
            JOptionPane.showMessageDialog(null, "Paciente no encontrado.");
        }
    }

    private static void registrarCita() {
        String nombrePaciente = JOptionPane.showInputDialog("Nombre del paciente:");
        Paciente paciente = buscarPaciente(nombrePaciente);
        if (paciente != null) {
            String fecha = JOptionPane.showInputDialog("Fecha (dd/mm/yyyy):");
            String hora = JOptionPane.showInputDialog("Hora (HH:mm):");
            String motivo = JOptionPane.showInputDialog("Motivo de la cita:");

            Cita cita = new Cita(fecha, hora, motivo, paciente);
            citas.add(cita);
            JOptionPane.showMessageDialog(null, "Cita registrada exitosamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Paciente no encontrado.");
        }
    }

    private static void eliminarCita() {
        String nombrePaciente = JOptionPane.showInputDialog("Ingrese el nombre del paciente:");
        String fecha = JOptionPane.showInputDialog("Ingrese la fecha de la cita a eliminar (dd/mm/yyyy):");

        citas.removeIf(c -> c.getPaciente().getNombre().equalsIgnoreCase(nombrePaciente) && c.getFecha().equals(fecha));
        JOptionPane.showMessageDialog(null, "Cita eliminada (si existía).");
    }

    private static void mostrarPacientes() {
        if (pacientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay pacientes registrados.");
        } else {
            StringBuilder lista = new StringBuilder();
            for (Paciente paciente : pacientes) {
                lista.append(paciente).append("\n");
            }
            JOptionPane.showMessageDialog(null, lista.toString(), "Lista de Pacientes", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void mostrarCitasPaciente() {
        String nombrePaciente = JOptionPane.showInputDialog("Nombre del paciente:");
        boolean encontrado = false;
        StringBuilder listaCitas = new StringBuilder();
        for (Cita cita : citas) {
            if (cita.getPaciente().getNombre().equalsIgnoreCase(nombrePaciente)) {
                listaCitas.append(cita).append("\n");
                encontrado = true;
            }
        }
        if (encontrado) {
            JOptionPane.showMessageDialog(null, listaCitas.toString(), "Citas del Paciente", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No hay citas registradas para el paciente.");
        }
    }

    private static Paciente buscarPaciente(String nombre) {
        for (Paciente paciente : pacientes) {
            if (paciente.getNombre().equalsIgnoreCase(nombre)) {
                return paciente;
            }
        }
        return null;
    }

    private static void cargarDatos() {
        try (ObjectInputStream oisPacientes = new ObjectInputStream(new FileInputStream(PACIENTES_FILE));
             ObjectInputStream oisCitas = new ObjectInputStream(new FileInputStream(CITAS_FILE))) {
            pacientes = (List<Paciente>) oisPacientes.readObject();
            citas = (List<Cita>) oisCitas.readObject();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se encontraron archivos previos. Se comenzará desde cero.");
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
        }
    }

    private static void guardarDatos() {
        try (ObjectOutputStream oosPacientes = new ObjectOutputStream(new FileOutputStream(PACIENTES_FILE));
             ObjectOutputStream oosCitas = new ObjectOutputStream(new FileOutputStream(CITAS_FILE))) {
            oosPacientes.writeObject(pacientes);
            oosCitas.writeObject(citas);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + e.getMessage());
        }
    }
}
