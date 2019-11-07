package com.lakeqiu.service.impl;

import com.lakeqiu.exception.ParamException;
import com.lakeqiu.mapper.SysUserMapper;
import com.lakeqiu.model.SysUser;
import com.lakeqiu.service.SysUserService;
import com.lakeqiu.utils.BeanValidator;
import com.lakeqiu.utils.MD5Util;
import com.lakeqiu.utils.PasswordUtil;
import com.lakeqiu.vo.UserParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author lakeqiu
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public void addUser(UserParam userParam) {
        BeanValidator.check(userParam);

        // 检查邮箱或手机是否已经被使用了
        if (checkEmailExist(userParam.getMail(), userParam.getId())) {
            throw new ParamException("邮箱已经被使用");
        }
        if (checkTelephoneExist(userParam.getTelephone(), userParam.getId())) {
            throw new ParamException("手机已经被使用");
        }

        // 随机生成密码并进行加密
        String password = PasswordUtil.randomPassword();
        password = MD5Util.encrypt(password);

        SysUser sysUser = SysUser.builder().username(userParam.getUsername()).telephone(userParam.getTelephone())
                .mail(userParam.getMail()).deptId(userParam.getDeptId())
                .status(userParam.getStatus()).remark(userParam.getRemark())
                .password("123456").build();

        sysUser.setOperator("System");
        sysUser.setOperateIp("127.0.0.1");
        sysUser.setOperateTime(new Date());

        // TODO 发送密码到邮箱

        sysUserMapper.insertSelective(sysUser);
    }

    @Override
    public void updateUser(UserParam userParam) {
        BeanValidator.check(userParam);

        // 检查邮箱或手机是否已经被使用了
        if (checkEmailExist(userParam.getMail(), userParam.getId())) {
            throw new ParamException("邮箱已经被使用");
        }
        if (checkTelephoneExist(userParam.getTelephone(), userParam.getId())) {
            throw new ParamException("手机已经被使用");
        }

        SysUser before = sysUserMapper.selectByPrimaryKey(userParam.getId());
        if (null == before) {
            throw new ParamException("要更新的用户不存在");
        }

        SysUser sysUser = SysUser.builder().id(userParam.getId()).username(userParam.getUsername())
                .telephone(userParam.getTelephone())
                .mail(userParam.getMail()).deptId(userParam.getDeptId())
                .status(userParam.getStatus()).remark(userParam.getRemark())
                .password(before.getPassword()).build();

        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public SysUser findByKeyword(String username) {
        return sysUserMapper.findByKeyword(username);
    }


    private boolean checkEmailExist(String mail, Integer userId) {
        return sysUserMapper.countByMail(mail, userId) > 0;
    }

    private boolean checkTelephoneExist(String telephone, Integer userId) {
        return sysUserMapper.countByTelephone(telephone, userId) > 0;
    }
}
