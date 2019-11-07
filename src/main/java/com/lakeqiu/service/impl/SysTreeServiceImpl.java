package com.lakeqiu.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.lakeqiu.dto.DeptLevelDto;
import com.lakeqiu.mapper.SysDeptMapper;
import com.lakeqiu.model.SysDept;
import com.lakeqiu.service.SysTreeService;
import com.lakeqiu.utils.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lakeqiu
 */
@Service
public class SysTreeServiceImpl implements SysTreeService {
    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Override
    public List<DeptLevelDto> deptTree() {
        List<SysDept> deptList = sysDeptMapper.getAllDept();
        List<DeptLevelDto> dtoList = Lists.newArrayList();
        // 将SysDept转化为DeptLevelDto并加入dtoList中
        deptList.stream().map(DeptLevelDto::adapt).forEach(dtoList::add);
        return deptListToTree(dtoList);
    }

    /**
     * 将传递进来的所有部门转化为树形结构
     * @param deptLevelDtoList 所有部门
     * @return List<DeptLevelDto> 其中下级部门挂在上级部门的list中
     */
    private List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelDtoList) {
        if (CollectionUtils.isEmpty(deptLevelDtoList)) {
            return Lists.newArrayList();
        }
        // 等于<String, List<DeptLevelDto>>
        Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
        // 将所有部门映射成<层级，部门>
        deptLevelDtoList.forEach(dept -> levelDeptMap.put(dept.getLevel(), dept));
        // 将顶级部门提取出来并按照seq从小到大排序
        List<DeptLevelDto> rootList = deptLevelDtoList.stream()
                .filter(dept -> LevelUtil.ROOT.equals(dept.getLevel())).sorted(Comparator.comparingInt(SysDept::getSeq)).collect(Collectors.toList());

        transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);
        return rootList;
    }

    /**
     * 递归获取设置部门的下一级部门，完成了就是树形结构
     * @param deptLevelDtoList 要设置下一级部门的部门列表（一般是顶级部门）
     * @param level 要设置下一级部门的部门的层级（用来计算下一级部门的层级）
     * @param levelDeptMap 所有部门<层级，部门>
     */
    private void transformDeptTree(List<DeptLevelDto> deptLevelDtoList, String level, Multimap<String, DeptLevelDto> levelDeptMap) {
        deptLevelDtoList.forEach(deptLevelDto -> {
            // 获取当前部门下一层级部门的level
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            // 根据level获取下一层级部门
            List<DeptLevelDto> nextDtoList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
            // 排序
            nextDtoList.sort(Comparator.comparingInt(SysDept::getSeq));
            // 将下一层级的部门设置在上一层级部门的属性上
            deptLevelDto.setDtoList(nextDtoList);
            transformDeptTree(nextDtoList, nextLevel, levelDeptMap);
        });
    }
}
