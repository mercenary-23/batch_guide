package com.apress.batch.chapter10.domain.customer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class CustomerContactPreparedStatementSetter implements ItemPreparedStatementSetter<CustomerUpdate> {

    @Override
    public void setValues(CustomerUpdate customer,
        PreparedStatement ps) throws SQLException {
        CustomerContactUpdate customerContact = (CustomerContactUpdate) customer;
        ps.setString(1, customerContact.getEmailAddress());
        ps.setString(2, customerContact.getHomePhone());
        ps.setString(3, customerContact.getCellPhone());
        ps.setString(4, customerContact.getWorkPhone());
        ps.setString(5, (customerContact.getNotificationPreferences() == null) ? null : customerContact.getNotificationPreferences().toString());
        ps.setLong(6, customerContact.getCustomerId());
    }

}
