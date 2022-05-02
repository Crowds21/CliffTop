package com.springclifftop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.*;

public class SelfTrackingAPI {

    @Autowired
    @Qualifier("sqlConnection")
    Connection connection;

    String Test=" INSERT INTO `time_table` VALUES( '202205021440-b23dzzd', '20220502','1440','hobby') ";

    public void insert(String sql) {
        try {
            Statement statement = connection.createStatement();
            // 查询数据
            ResultSet resultSet = statement.executeQuery(sql);

            // 关闭语句和连接
            resultSet.close();
            statement.close();
            //connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}

