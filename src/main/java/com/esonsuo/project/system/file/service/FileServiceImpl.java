package com.esonsuo.project.system.file.service;

import java.util.List;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.esonsuo.common.config.MinioConfig;
import com.esonsuo.common.constant.UserConstants;
import com.esonsuo.common.exception.ServiceException;
import com.esonsuo.common.utils.StringUtils;
import com.esonsuo.common.utils.UUIDUtils;
import com.esonsuo.common.utils.bean.BeanValidators;
import com.esonsuo.common.utils.file.FileUploadUtils;
import com.esonsuo.common.utils.security.ShiroUtils;
import com.esonsuo.common.utils.text.Convert;
import com.esonsuo.framework.aspectj.lang.annotation.DataScope;
import com.esonsuo.framework.web.domain.AjaxResult;
import com.esonsuo.project.system.file.domain.File;
import com.esonsuo.project.system.file.mapper.FileMapper;

/**
 * 文件 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class FileServiceImpl implements IFileService
{
    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    protected Validator validator;

    /**
     * 根据条件分页查询文件列表
     * 
     * @param file 文件信息
     * @return 文件信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", fileAlias = "f")
    public List<File> selectFileList(File file)
    {
        // 生成数据权限过滤条件
        return fileMapper.selectFileList(file);
    }


    /**
     * 通过文件名查询文件
     * 
     * @param fileName 文件名
     * @return 文件对象信息
     */
    @Override
    public File selectFileByLoginName(String fileName)
    {
        return fileMapper.selectFileByLoginName(fileName);
    }

    /**
     * 通过文件ID查询文件
     * 
     * @param fileId 文件ID
     * @return 文件对象信息
     */
    @Override
    public File selectFileById(Long fileId)
    {
        return fileMapper.selectFileById(fileId);
    }

    /**
     * 通过文件ID删除文件
     * 
     * @param fileId 文件ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteFileById(Long fileId)
    {

        return fileMapper.deleteFileById(fileId);
    }

    /**
     * 批量删除文件信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteFileByIds(String ids)
    {
        Long[] fileIds = Convert.toLongArray(ids);
        // for (Long fileId : fileIds)
        // {
            // checkFileAllowed(new File(fileId));
            // checkFileDataScope(fileId);
        // }

        return fileMapper.deleteFileByIds(fileIds);
    }

    /**
     * 新增保存文件信息
     * 
     * @param file 文件信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertFile(File file)
    {
        // file.setCreateBy(ShiroUtils.getLoginName());
        // 新增文件信息
        int rows = fileMapper.insertFile(file);

        return rows;
    }

    /**
     * 修改保存文件信息
     * 
     * @param file 文件信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateFile(File file)
    {
        file.setUpdateBy(ShiroUtils.getLoginName());
        return fileMapper.updateFile(file);
    }

    /**
     * 修改文件信息
     * 
     * @param file 文件信息
     * @return 结果
     */
    @Override
    public int updateFileInfo(File file)
    {
        return fileMapper.updateFile(file);
    }

    /**
     * 校验文件名称是否唯一
     * 
     * @param file 文件信息
     * @return 结果
     */
    @Override
    public boolean checkLoginNameUnique(File file)
    {
        String fileId =  file.getFileId();
        File info = fileMapper.checkLoginNameUnique(file.getFileName());
        if (StringUtils.isNotNull(info) && info.getFileId().equals(fileId))
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验文件是否有数据权限
     * 
     * @param fileId 文件id
     */
    // @Override
    // public void checkFileDataScope(Long fileId)
    // {
    //     if (!File.isAdmin(ShiroUtils.getFileId()))
    //     {
    //         File file = new File();
    //         file.setFileId(fileId);
    //         List<File> files = SpringUtils.getAopProxy(this).selectFileList(file);
    //         if (StringUtils.isEmpty(files))
    //         {
    //             throw new ServiceException("没有权限访问文件数据！");
    //         }
    //     }
    // }

    /**
     * 导入文件数据
     * 
     * @param fileList 文件数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    @Override
    public String importFile(List<File> fileList, Boolean isUpdateSupport)
    {
        if (StringUtils.isNull(fileList) || fileList.size() == 0)
        {
            throw new ServiceException("导入文件数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String operName = ShiroUtils.getLoginName();
        for (File file : fileList)
        {
            try
            {
                // 验证是否存在这个文件
                File u = fileMapper.selectFileByLoginName(file.getFileName());
                if (StringUtils.isNull(u))
                {
                    BeanValidators.validateWithException(validator, file);
                    file.setCreateBy(operName);
                    fileMapper.insertFile(file);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + file.getFileName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, file);
                    // checkFileAllowed(u);
                    // checkFileDataScope(u.getFileId());
                    file.setFileId(u.getFileId());
                    file.setUpdateBy(operName);
                    fileMapper.updateFile(file);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + file.getFileName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + file.getFileName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + file.getFileName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 文件状态修改
     * 
     * @param file 文件信息
     * @return 结果
     */
    @Override
    public int changeStatus(File file)
    {
        return fileMapper.updateFile(file);
    }

    /**
     * Minio默认上传的地址
     */
    private static String bucketName = MinioConfig.getBucketName();

    private static String url = MinioConfig.getUrl();

    public String getBucketName() {
        return bucketName;
    }
    
    @Override
    public AjaxResult uploadFile(MultipartFile file, File info) {
        try {
            // 上传并返回新文件名称
            String filePath = FileUploadUtils.uploadMinio(file);
            info.setFileId(UUIDUtils.getInstance().generateShortUuid());
            info.setFileName(file.getOriginalFilename());
            info.setFileType("1");
            info.setBucket(bucketName);
            info.setFileSize(String.valueOf(file.getSize()));
            info.setfilePath(filePath.replaceAll(url + "/" + bucketName + "/", ""));
            this.insertFile(info);
            return AjaxResult.success(filePath.replaceAll(url + "/" + bucketName + "/", ""));
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return AjaxResult.error(e.getMessage());
        }
    }
}
