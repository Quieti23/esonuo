package com.esonsuo.project.system.file.service;

import com.esonsuo.framework.web.domain.AjaxResult;
import com.esonsuo.project.system.file.domain.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件 业务层
 * 
 * @author ruoyi
 */
public interface IFileService
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
     * @throws Exception 异常
     */
    public int deleteFileByIds(String ids);

    /**
     * 保存文件信息
     * 
     * @param file 文件信息
     * @return 结果
     */
    public int insertFile(File file);

    /**
     * 保存文件信息
     * 
     * @param file 文件信息
     * @return 结果
     */
    public int updateFile(File file);

    /**
     * 修改文件详细信息
     * 
     * @param file 文件信息
     * @return 结果
     */
    public int updateFileInfo(File file);

    /**
     * 校验文件名称是否唯一
     * 
     * @param file 文件信息
     * @return 结果
     */
    public boolean checkLoginNameUnique(File file);

    /**
     * 校验文件是否有数据权限
     * 
     * @param fileId 文件id
     */
    // public void checkFileDataScope(Long fileId);

    /**
     * 导入文件数据
     * 
     * @param fileList 文件数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    public String importFile(List<File> fileList, Boolean isUpdateSupport);

    /**
     * 文件状态修改
     * 
     * @param file 文件信息
     * @return 结果
     */
    public int changeStatus(File file);

    /**
     * 上传文件
     * 
     * @param file 文件对象
     * @return 文件名称
     */
    public AjaxResult uploadFile(MultipartFile file, File info);
}
