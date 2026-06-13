package com.tablebit.mobile.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DashboardData {

    @SerializedName("reservas_hoy")
    private int reservasHoy;

    private int confirmadas;
    private int canceladas;

    @SerializedName("ocupacion_hoy")
    private double ocupacionHoy;

    @SerializedName("mesas_totales")
    private int mesasTotales;

    @SerializedName("mesas_ocupadas_hoy")
    private int mesasOcupadasHoy;

    @SerializedName("mesas_libres_hoy")
    private int mesasLibresHoy;

    @SerializedName("personas_promedio")
    private double personasPromedio;

    @SerializedName("horas_pico")
    private List<HoraPico> horasPico;

    @SerializedName("reservas_por_semana")
    private List<ReservaSemana> reservasPorSemana;

    public int getReservasHoy() { return reservasHoy; }
    public void setReservasHoy(int reservasHoy) { this.reservasHoy = reservasHoy; }
    public int getConfirmadas() { return confirmadas; }
    public void setConfirmadas(int confirmadas) { this.confirmadas = confirmadas; }
    public int getCanceladas() { return canceladas; }
    public void setCanceladas(int canceladas) { this.canceladas = canceladas; }
    public double getOcupacionHoy() { return ocupacionHoy; }
    public void setOcupacionHoy(double ocupacionHoy) { this.ocupacionHoy = ocupacionHoy; }
    public int getMesasTotales() { return mesasTotales; }
    public void setMesasTotales(int mesasTotales) { this.mesasTotales = mesasTotales; }
    public int getMesasOcupadasHoy() { return mesasOcupadasHoy; }
    public void setMesasOcupadasHoy(int mesasOcupadasHoy) { this.mesasOcupadasHoy = mesasOcupadasHoy; }
    public int getMesasLibresHoy() { return mesasLibresHoy; }
    public void setMesasLibresHoy(int mesasLibresHoy) { this.mesasLibresHoy = mesasLibresHoy; }
    public double getPersonasPromedio() { return personasPromedio; }
    public void setPersonasPromedio(double personasPromedio) { this.personasPromedio = personasPromedio; }
    public List<HoraPico> getHorasPico() { return horasPico; }
    public void setHorasPico(List<HoraPico> horasPico) { this.horasPico = horasPico; }
    public List<ReservaSemana> getReservasPorSemana() { return reservasPorSemana; }
    public void setReservasPorSemana(List<ReservaSemana> reservasPorSemana) { this.reservasPorSemana = reservasPorSemana; }

    public String getCapacidadFisica() {
        return mesasOcupadasHoy + "/" + mesasTotales;
    }

    public String getEstadoDescripcion() {
        if (reservasHoy == 0) return "Tranquilo — Sin reservas hoy";
        if (reservasHoy <= 3) return "Normal — " + reservasHoy + " reserva" + (reservasHoy > 1 ? "s" : "") + " hoy";
        return "Ocupado — " + reservasHoy + " reservas programadas";
    }

    public String getOcupacionDetalle() {
        return ocupacionHoy + "% — " + getCapacidadFisica() + " mesas hoy";
    }

    public static class HoraPico {
        private int hora;
        private int total;
        public int getHora() { return hora; }
        public void setHora(int hora) { this.hora = hora; }
        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }
    }

    public static class ReservaSemana {
        private int semana;
        private int total;
        public int getSemana() { return semana; }
        public void setSemana(int semana) { this.semana = semana; }
        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }
    }
}
