package com.esonsuo.project.system.file.domain;

import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.esonsuo.common.xss.Xss;
import com.esonsuo.framework.aspectj.lang.annotation.Excel;
import com.esonsuo.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.esonsuo.framework.aspectj.lang.annotation.Excel.Type;
import com.esonsuo.framework.aspectj.lang.annotation.Excels;
import com.esonsuo.framework.web.domain.BaseEntity;
import com.esonsuo.project.system.dept.domain.Dept;

/**
 * 文件对象 sys_file
 * 
 * @author ruoyi
 */
public class File extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件ID */
    @Excel(name = "文件序号", cellType = ColumnType.NUMERIC, prompt = "文件编号")
    private String fileId;

    /** 部门ID */
    @Excel(name = "部门编号", type = Type.IMPORT)
    private Long deptId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    /** 文件类型 */
    private String fileType;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private String fileSize;

    private String filePath;

    private String bucket;

    /** 文件状态（0正常 1停用） */
    @Excel(name = "文件状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 部门对象 */
    @Excels({
        @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
        @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    private Dept dept;

    public File()
    {

    }

    public File(String fileId)
    {
        this.fileId = fileId;
    }

    public String getFileId()
    {
        return fileId;
    }

    public void setFileId(String fileId)
    {
        this.fileId = fileId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    @Xss(message = "文件昵称不能包含脚本字符")
    @Size(min = 0, max = 30, message = "文件昵称长度不能超过30个字符")
    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileType()
    {
        return fileType;
    }

    public String getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(String fileSize)
    {
        this.fileSize = fileSize;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public String getfilePath() {
        return this.filePath;
    }

    public void setfilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public Dept getDept()
    {
        if (dept == null)
        {
            dept = new Dept();
        }
        return dept;
    }

    public void setDept(Dept dept)
    {
        this.dept = dept;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("fileId", getFileId())
            .append("deptId", getDeptId())
            .append("fileName", getFileName())
            .append("fileType", getFileType())
            .append("fileSize", getFileSize())
            .append("filePath", getfilePath())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("dept", getDept())
            .toString();
    }
}
