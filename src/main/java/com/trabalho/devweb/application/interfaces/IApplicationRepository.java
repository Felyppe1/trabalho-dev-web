package com.trabalho.devweb.application.interfaces;

import com.trabalho.devweb.domain.Application;
import java.sql.SQLException;

public interface IApplicationRepository {
    void save(Application application) throws SQLException;
}
