package com.education.service;

import com.education.db.entity.BackendLoginHistoryEntity;
import com.education.db.entity.BackendRoleEntity;
import com.education.db.entity.BackendUserEntity;
import com.education.db.jpa.BackendLoginHistoryRepository;
import com.education.db.jpa.BackendRoleRepository;
import com.education.db.jpa.BackendUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 11/9/15.
 */
@Service("BackendLoginService")
public class BackendLoginService {

    @Autowired
    private BackendUserRepository userRepository;

    @Autowired
    private BackendLoginHistoryRepository loginHistoryRepository;

    @Autowired
    private BackendRoleRepository roleRepository;

    private static final Logger logger = Logger.getLogger(BackendLoginService.class.getName());

    public boolean whetherLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getAttribute("user_name") != null;
    }

    @Transactional
    public boolean login(String userName, String password) {
        logger.info("login user " + userName + " password:" + password);
        List<BackendUserEntity> userList = userRepository.findByNameAndPassword(userName, password);
        boolean login = false;
        if (userList != null && userList.size() > 0) {
            login = true;
        }
        List<BackendUserEntity> userNameList = userRepository.findByName(userName);
        if (!userNameList.isEmpty()) {
            BackendLoginHistoryEntity entity = new BackendLoginHistoryEntity();
            entity.setLoginStatus(login ? 1 : 0);
            entity.setLoginTime(Calendar.getInstance().getTime());
            entity.setUserId(userNameList.get(0).getId());
            loginHistoryRepository.save(entity);
        }
        return login;
    }

    public String getUserRole(String userName, String password){
        List<BackendUserEntity> userList = userRepository.findByNameAndPassword(userName, password);
        if(!userList.isEmpty()){
            int roleId = userList.get(0).getRoleId();
            BackendRoleEntity role = roleRepository.findOne(roleId);
            return role.getRole();
        }
        return null;
    }

    public String getUserRole(String userName){
        List<BackendUserEntity> userEntityList = userRepository.findByName(userName);
        if(!userEntityList.isEmpty()){
            int roleId = userEntityList.get(0).getRoleId();
            BackendRoleEntity role = roleRepository.findOne(roleId);
            return role.getRole();
        }
        return null;
    }
}
