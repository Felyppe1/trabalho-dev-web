package com.trabalho.devweb.application.interfaces;

import com.trabalho.devweb.domain.Application;
import java.sql.SQLException;
import java.util.List;

public interface IApplicationRepository {
    void save(Application application) throws SQLException;

    List<Application> findByAccountIdAndCategoryAndYear(String accountId, String category, int year)
            throws SQLException;

    void delete(String applicationId) throws SQLException;

    void updateAmount(String applicationId, java.math.BigDecimal newAmount) throws SQLException;
}
