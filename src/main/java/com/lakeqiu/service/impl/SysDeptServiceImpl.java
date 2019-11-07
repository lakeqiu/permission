package com.lakeqiu.service.impl;

import com.google.common.base.Preconditions;
import com.lakeqiu.exception.ParamException;
import com.lakeqiu.mapper.SysDeptMapper;
import com.lakeqiu.model.SysDept;
import com.lakeqiu.service.SysDeptService;
import com.lakeqiu.utils.BeanValidator;
import com.lakeqiu.utils.LevelUtil;
import com.lakeqiu.vo.DeptParam;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author lakeqiu
 */
@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Override
    public void saveDept(DeptParam deptParam) {
        // 检查参数是否合法
        BeanValidator.check(deptParam);
        // 检查是否存在同一个部门
        if (checkExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())){
            throw new ParamException("存在相同的部门");
        }
        // 创建部门信息类
        SysDept sysDept = SysDept.builder().name(deptParam.getName()).parentId(deptParam.getParentId())
                .seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();
        // 获取计算出来的当前部门层级关系
        sysDept.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()), deptParam.getParentId()));
        // TODO
        sysDept.setOperator("System");
        // TODO
        sysDept.setOperateIp("127.0.0.1");
        sysDept.setOperateTime(new Date());
        // 插入数据库，这个方法会判断值是否为空，为空就不插入，与insert相比，可以避开not null约束
        sysDeptMapper.insertSelective(sysDept);
    }

    @Override
    public void updateDept(DeptParam deptParam) {
        BeanValidator.check(deptParam);
        // 检查数据库，看一下有没有要更新的部门
        SysDept before = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
        if (null == before) {
            throw new ParamException("待更新的部门不存在");
        }
        /*Preconditions.checkNotNull(before, "待更新的部门不存在");*/

        if (checkExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new ParamException("存在相同的部门");
        }
        // 创建部门信息类
        SysDept dept = SysDept.builder().name(deptParam.getName()).parentId(deptParam.getParentId())
                .seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();
        // 获取计算出来的当前部门层级关系
        dept.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()), deptParam.getParentId()));
        // TODO
        dept.setOperator("System");
        // TODO
        dept.setOperateIp("127.0.0.1");
        dept.setOperateTime(new Date());
        updateWithChild(before, dept);
    }

    /**
     * 为什么需要更新
     *  如 原先有一个部门其id为 1（父部门为0），那么其子部门level为 0.1.1， 0.1.2
     *  如果这时候将其父部门修改为id为2的部门，那么子部门就需要改为2.1.1， 2.1.2
     * @param before
     * @param after
     */
    @Transactional
    void updateWithChild(SysDept before, SysDept after){
        String beforeLevel = before.getLevel();
        String afterLevel = after.getLevel();

        // 查看子部门是否需要更新
        if (!beforeLevel.equals(afterLevel)) {
            List<SysDept> childDeptList = sysDeptMapper.getChildDeptListByLevel(beforeLevel);
            // 如果有子部门
            if (CollectionUtils.isNotEmpty(childDeptList)){
                // 找出level等于未更新前的level的，将其更新
                childDeptList.stream().filter(dept -> dept.getLevel().indexOf(beforeLevel) == 0)
                        .forEach(dept -> dept.setLevel(afterLevel + dept.getLevel().substring(beforeLevel.length())));
            }
            // 批量插入
            sysDeptMapper.batchUpdateLevel(childDeptList);
        }
        sysDeptMapper.updateByPrimaryKeySelective(after);
    }

    /**
     * 检查当前部门是否存在
     * @param parentId 部门的上级部门id
     * @param deptName 部门名字
     * @param deptId 部门id
     * @return 结果
     */
    private boolean checkExist(Integer parentId, String deptName, Integer deptId){
        return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }

    /**
     * 获取当前部门层级
     * @param deptId 当前部门id
     * @return 当前部门层级
     */
    private String getLevel(Integer deptId){
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
        // 如果当前部门为空，说明其的层级应该是第一级，故返回null
        return null == sysDept ? null : sysDept.getLevel();
    }
}
