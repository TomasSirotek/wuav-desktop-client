package com.wuav.client.dal.repository;

import com.wuav.client.be.Address;
import com.wuav.client.be.CustomImage;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.dal.interfaces.IAddressRepository;
import com.wuav.client.dal.mappers.AddressMapper;
import com.wuav.client.dal.mappers.ImageMapper;
import com.wuav.client.dal.mappers.UserMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import com.wuav.client.gui.dto.AddressDTO;
import javafx.fxml.FXML;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class AddressRepository implements IAddressRepository {
    static Logger logger = LoggerFactory.getLogger(AddressRepository.class);


    @Override
    public int createAddress(AddressDTO addressDTO) {
        int affectedRowsResult = 0;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            AddressMapper mapper = session.getMapper(AddressMapper.class);
            var affectedRows = mapper.createAddress(
                    addressDTO.id(),
                    addressDTO.street(),
                    addressDTO.city(),
                    addressDTO.zipCode()
            );

            session.commit();
            affectedRowsResult = affectedRows > 0 ? 1 : 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return affectedRowsResult;
    }


    @Override
    public Address getAddressById(int id) {
       Address address = null;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            AddressMapper mapper = session.getMapper(AddressMapper.class);
            address = mapper.getAddressById(id);
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return address;
    }

}
