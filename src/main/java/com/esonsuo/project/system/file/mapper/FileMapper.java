package com.esonsuo.project.system.file.mapper;

import com.esonsuo.project.system.file.domain.File;
import java.util.List;

/**
 * 文件表 数据层
 * 
 * @author ruoyi
 */
public interface FileMapper
{
    /**
     * 根据条件分页查询文件列表
     * 
     * @param file 文件信息
     * @return 文件信息集合信息
     */
    public List<File> selectFileList(File file);

    /**
     * 通过文件名查询文件
     * 
     * @param fileName 文件名
     * @return 文件对象信息
     */
    public File selectFileByLoginName(String fileName);

    /**
     * 通过文件ID查询文件
     * 
     * @param fileId 文件ID
     * @return 文件对象信息
     */
    public File selectFileById(Long fileId);

    /**
     * 通过文件ID删除文件
     * 
     * @param fileId 文件ID
     * @return 结果
     */
    public int deleteFileById(Long fileId);

    /**
     * 批量删除文件信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFileByIds(Long[] ids);

    /**
     * 修改文件信息
     * 
     * @param file 文件信息
     * @return 结果
     */
    public int updateFile(File file);

    /**
     * 新增文件信息
     * 
     * @param file 文件信息
     * @return 结果
     */
    public int insertFile(File file);

    /**
     * 校验文件名称是否唯一
     * 
     * @param loginName 登录名称
     * @return 结果
     */
    public File checkLoginNameUnique(String loginName);

}
