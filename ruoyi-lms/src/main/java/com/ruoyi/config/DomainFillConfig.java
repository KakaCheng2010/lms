package com.ruoyi.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author cheng
 * 自动填充 创建时间和修改时间
 */
@Component
@Log4j2
public class DomainFillConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        //设置属性值
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);

        //操作人
        LoginUser loginUser = SecurityUtils.getLoginUserWithNull();
        if (loginUser != null) {
            this.setFieldValByName("createBy", loginUser.getUsername(), metaObject);
            this.setFieldValByName("updateBy", loginUser.getUsername(), metaObject);
        }


    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //设置属性值
        this.setFieldValByName("updateTime", new Date(), metaObject);
        //操作人

        LoginUser loginUser = SecurityUtils.getLoginUserWithNull();
        if (loginUser != null) {
            this.setFieldValByName("updateBy", loginUser.getUsername(), metaObject);
        }

    }
}