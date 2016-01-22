package com.education.service;

import com.education.db.entity.UserEntity;
import com.education.db.jpa.UserRepository;
import com.education.service.converter.WeChatUserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by yzzhao on 1/15/16.
 */
@Service("OnlineUserService")
public class BackendOnlineUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeChatUserConverter weChatUserConverter;

    public List<WeChatUserInfo> getUserList(int pageIdx, int number){
        Page<UserEntity> entities = userRepository.findAll(createPagable(pageIdx, number));
        List<WeChatUserInfo> userInfoList =new ArrayList<>();
        Page<WeChatUserInfo> map = entities.map(weChatUserConverter);
        map.forEach(new Consumer<WeChatUserInfo>() {
            @Override
            public void accept(WeChatUserInfo userInfo) {
                userInfoList.add(userInfo);
            }
        });
        return userInfoList;
    }

    private Pageable createPagable(int pageIdx, int number){
        return new PageRequest(pageIdx, number, new Sort(Sort.Direction.DESC, "userId"));
    }
}