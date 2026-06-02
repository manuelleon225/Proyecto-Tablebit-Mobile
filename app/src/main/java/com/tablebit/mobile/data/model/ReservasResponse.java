package com.tablebit.mobile.data.model;

import java.util.List;

public class ReservasResponse {
    private int current_page;
    private List<Reserva> data;
    private int last_page;
    private int total;

    public List<Reserva> getData() { return data; }
    public int getTotal() { return total; }
}
