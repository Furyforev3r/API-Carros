package com.furyforever.dao;

import com.furyforever.exceptions.EntidadeNaoEncontradaException;
import com.furyforever.model.Carro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarroDao {
    public static final String SQL_SELECT_ALL = "SELECT * FROM TBL_VEICULOS";
    public static final String SQL_SELECT_BY_CHASSI = "SELECT * FROM TBL_VEICULOS WHERE chassi = ?";
    public  static final String SQL_INSERT = "INSERT INTO TBL_VEICULOS (chassi, placa, cor, motor, marca, modelo) VALUES (?, ?, ?, ?, ?, ?)";
    public  static final String SQL_UPDATE = "UPDATE TBL_VEICULOS SET chassi = ?, placa = ?, cor = ?, motor = ?, marca = ?, modelo = ? WHERE chassi = ?";
    public  static final String SQL_DELETE = "DELETE FROM TBL_VEICULOS WHERE chassi = ?";

    private Connection connection;

    public CarroDao(Connection connection) {
        this.connection = connection;
    }

    public void cadastrar(Carro carro) throws SQLException {
        try (PreparedStatement stm = connection.prepareStatement(SQL_INSERT)) {
            preencherStatement(carro, stm);
            stm.executeUpdate();
        }
    }

    public void atualizar(Carro carro) throws SQLException, EntidadeNaoEncontradaException {
        try (PreparedStatement stm = connection.prepareStatement(SQL_UPDATE)) {
            preencherStatement(carro, stm);
            stm.setString(7, carro.getChassi());


            Integer linhas = stm.executeUpdate();

            if (linhas == 0)
                throw new EntidadeNaoEncontradaException("Carro não encontrado!");
        }
    }

    private static void preencherStatement(Carro carro, PreparedStatement stm) throws SQLException {
        stm.setString(1, carro.getChassi());
        stm.setString(2, carro.getPlaca());
        stm.setString(3, carro.getCor());
        stm.setString(4, carro.getMotor());
        stm.setString(5, carro.getMarca());
        stm.setString(6, carro.getModelo());
    }

    public Carro procurarPorChassi(String chassi) throws SQLException, EntidadeNaoEncontradaException {
        try (PreparedStatement stm = connection.prepareStatement(SQL_SELECT_BY_CHASSI)) {
            stm.setString(1, chassi);
            try (ResultSet resultSet = stm.executeQuery()) {
                if (resultSet.next()) {
                    Carro carro = parseCarro(resultSet);

                    return carro;
                } else {
                    throw new EntidadeNaoEncontradaException("Carro não encontrado!");
                }
            }
        }
    }

    public List<Carro> listar() throws SQLException {
        try(PreparedStatement stm = connection.prepareStatement(SQL_SELECT_ALL)) {
            try (ResultSet resultSet = stm.executeQuery()) {
                List<Carro> lista = new ArrayList<>();
                while (resultSet.next()) {
                    Carro carro = parseCarro(resultSet);

                    lista.add(carro);
                }

                return lista;
            }
        }
    }

    private static Carro parseCarro(ResultSet resultSet) throws SQLException {
        Carro carro = new Carro();

        carro.setChassi(resultSet.getString("chassi"));
        carro.setPlaca(resultSet.getString("placa"));
        carro.setCor(resultSet.getString("cor"));
        carro.setMotor(resultSet.getString("motor"));
        carro.setMarca(resultSet.getString("marca"));
        carro.setModelo(resultSet.getString("modelo"));
        return carro;
    }

    public void remover(String chassi) throws SQLException, EntidadeNaoEncontradaException {
        try(PreparedStatement stm = connection.prepareStatement(SQL_DELETE)) {
            stm.setString(1, chassi);
            Integer linhas = stm.executeUpdate();

            if (linhas == 0)
                throw new EntidadeNaoEncontradaException("Carro não encontrado!");
        }
    }
}
