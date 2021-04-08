package org.fesoft.ormmicro.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListHandler<T> {


    public List<T> readList(ResultSet rs, Class entityClass) throws SQLException {
        List<T> entityList = new ArrayList<>();
        RowHandler<T> rowHandler = new RowHandler<>();
        while (rs.next()) {
            entityList.add((T) rowHandler.readEntity(rs, entityClass));
        }
        return entityList;
    }


}
