package com.my.json.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public final class ConexionDB {

    private static final HikariDataSource DATA_SOURCE = crearDataSource();

    private ConexionDB() {
    }

    private static HikariDataSource crearDataSource() {
        HikariConfig config = new HikariConfig();
        config.setPoolName("inscripciones-pool");
        config.setDriverClassName("org.postgresql.Driver");

        config.setJdbcUrl(obtener(
                "DB_URL",
                "jdbc:postgresql://dpg-dc35ne8m7kps73d8sn8g-a:5432/actividades_complementarias_bd?sslmode=require&stringtype=unspecified"
        ));
        config.setUsername(obtener("DB_USER", "palito"));
        config.setPassword(obtener("DB_PASSWORD", "nff35zUR6vloL5oElDPHwQjxoV63Jex9"));

        config.setMaximumPoolSize(entero("DB_MAX_POOL_SIZE", 10));
        config.setMinimumIdle(entero("DB_MIN_IDLE", 2));
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.addDataSourceProperty("stringtype", "unspecified");
        return new HikariDataSource(config);
    }

    private static String obtener(String nombre, String valorPorDefecto) {
        String valor = System.getProperty(nombre, System.getenv(nombre));
        return valor == null || valor.isBlank() ? valorPorDefecto : valor;
    }

    private static int entero(String nombre, int valorPorDefecto) {
        try {
            return Integer.parseInt(obtener(nombre, String.valueOf(valorPorDefecto)));
        } catch (NumberFormatException e) {
            return valorPorDefecto;
        }
    }

    public static Connection getConexion() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    public static void cerrar() {
        if (!DATA_SOURCE.isClosed()) {
            DATA_SOURCE.close();
        }
    }
}
