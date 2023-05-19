package com.esonsuo.project.system.file.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.esonsuo.common.config.MinioConfig;
import com.esonsuo.common.utils.StringUtils;
import com.esonsuo.common.utils.poi.ExcelUtil;
import com.esonsuo.common.utils.security.AuthorizationUtils;
import com.esonsuo.common.utils.spring.SpringUtils;
import com.esonsuo.framework.aspectj.lang.annotation.Log;
import com.esonsuo.framework.aspectj.lang.enums.BusinessType;
import com.esonsuo.framework.web.controller.BaseController;
import com.esonsuo.framework.web.domain.AjaxResult;
import com.esonsuo.framework.web.domain.Ztree;
import com.esonsuo.framework.web.page.TableDataInfo;
import com.esonsuo.project.system.dept.domain.Dept;
import com.esonsuo.project.system.dept.service.IDeptService;
import com.esonsuo.project.system.file.domain.File;
import com.esonsuo.project.system.file.service.IFileService;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;

/**
 * 文件信息
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/file")
public class FileController extends BaseController
{
    private String prefix = "system/file";

    @Autowired
    private IFileService fileService;

    @Autowired
    private IDeptService deptService;

    @RequiresPermissions("system:file:view")
    @GetMapping()
    public String file()
    {
        return prefix + "/file";
    }

    @RequiresPermissions("system:file:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(File file)
    {
        startPage();
        List<File> list = fileService.selectFileList(file);
        return getDataTable(list);
    }

    @Log(title = "文件管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:file:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(File file)
    {
        List<File> list = fileService.selectFileList(file);
        ExcelUtil<File> util = new ExcelUtil<File>(File.class);
        return util.exportExcel(list, "文件数据");
    }

    @Log(title = "文件管理", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:file:import")
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<File> util = new ExcelUtil<File>(File.class);
        List<File> fileList = util.importExcel(file.getInputStream());
        String message = fileService.importFile(fileList, updateSupport);
        return AjaxResult.success(message);
    }

    @RequiresPermissions("system:file:view")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate()
    {
        ExcelUtil<File> util = new ExcelUtil<File>(File.class);
        return util.importTemplateExcel("文件数据");
    }

    /**
     * 新增文件
     */
    // @GetMapping("/add")
    // public String add(ModelMap mmap)
    // {
    //     mmap.put("roles", roleService.selectRoleAll().stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
    //     mmap.put("posts", postService.selectPostAll());
    //     return prefix + "/add";
    // }

    /**
     * 新增保存文件
     */
    @RequiresPermissions("system:file:add")
    @Log(title = "文件管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated File file)
    {
        // if (!fileService.checkLoginNameUnique(file))
        // {
        //     return error("新增文件'" + file.getLoginName() + "'失败，登录账号已存在");
        // }
        // else if (StringUtils.isNotEmpty(file.getPhonenumber()) && !fileService.checkPhoneUnique(file))
        // {
        //     return error("新增文件'" + file.getLoginName() + "'失败，手机号码已存在");
        // }
        // else if (StringUtils.isNotEmpty(file.getEmail()) && !fileService.checkEmailUnique(file))
        // {
        //     return error("新增文件'" + file.getLoginName() + "'失败，邮箱账号已存在");
        // }
        return toAjax(fileService.insertFile(file));
    }

    /**
     * 修改文件
     */
    @RequiresPermissions("system:file:edit")
    @GetMapping("/edit/{fileId}")
    public String edit(@PathVariable("fileId") Long fileId, ModelMap mmap)
    {
        // fileService.checkFileDataScope(fileId);
        // List<Role> roles = roleService.selectRolesByFileId(fileId);
        mmap.put("file", fileService.selectFileById(fileId));
        // mmap.put("roles", File.isAdmin(fileId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        // mmap.put("posts", postService.selectPostsByFileId(fileId));
        return prefix + "/edit";
    }

    /**
     * 修改保存文件
     */
    @RequiresPermissions("system:file:edit")
    @Log(title = "文件管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated File file)
    {
        // fileService.checkFileAllowed(file);
        // fileService.checkFileDataScope(file.getFileId());
        // if (!fileService.checkLoginNameUnique(file))
        // {
        //     return error("修改文件'" + file.getLoginName() + "'失败，登录账号已存在");
        // }
        // else if (StringUtils.isNotEmpty(file.getPhonenumber()) && !fileService.checkPhoneUnique(file))
        // {
        //     return error("修改文件'" + file.getLoginName() + "'失败，手机号码已存在");
        // }
        // else if (StringUtils.isNotEmpty(file.getEmail()) && !fileService.checkEmailUnique(file))
        // {
        //     return error("修改文件'" + file.getLoginName() + "'失败，邮箱账号已存在");
        // }
        AuthorizationUtils.clearAllCachedAuthorizationInfo();
        return toAjax(fileService.updateFile(file));
    }

    /**
     * 校验文件名
     */
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public boolean checkLoginNameUnique(File file)
    {
        return fileService.checkLoginNameUnique(file);
    }

    /**
     * 文件状态修改
     */
    @Log(title = "文件管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:file:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(File file)
    {
        // fileService.checkFileAllowed(file);
        // fileService.checkFileDataScope(file.getFileId());
        return toAjax(fileService.changeStatus(file));
    }

    /**
     * 加载部门列表树
     */
    @RequiresPermissions("system:file:list")
    @GetMapping("/deptTreeData")
    @ResponseBody
    public List<Ztree> deptTreeData()
    {
        List<Ztree> ztrees = deptService.selectDeptTree(new Dept());
        return ztrees;
    }

    /**
     * 选择部门树
     * 
     * @param deptId 部门ID
     */
    @RequiresPermissions("system:file:list")
    @GetMapping("/selectDeptTree/{deptId}")
    public String selectDeptTree(@PathVariable("deptId") Long deptId, ModelMap mmap)
    {
        mmap.put("dept", deptService.selectDeptById(deptId));
        return prefix + "/deptTree";
    }

    /**
     * 
     * 文件上传请求
     */
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult uploadFile(MultipartFile file, @RequestParam("deptId") Long deptId) throws IOException {
        String fileName = file.getOriginalFilename();
        File file2 = new File();
        file2.setFileName(fileName);
        file2.setDeptId(deptId);
        return fileService.uploadFile(file, file2);
    }

    /**
     * Minio默认上传的地址
     */
    private static String bucketName = MinioConfig.getBucketName();

    public String getBucketName() {
        return bucketName;
    }

    /**
     * 
     * 文件下载
     * @throws IllegalArgumentException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String bucketName,
                                                            @RequestParam String filePath, 
                                                            @RequestParam String fileName) throws Exception {
        try {
                MinioClient minioClient = SpringUtils.getBean(MinioClient.class);
                // Download the file from the Minio server
                InputStream inputStream = minioClient.getObject(
                        GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filePath)
                        .build());

                InputStreamResource resource = new InputStreamResource(inputStream);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()) + "\"");
                return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
            } catch (IOException | MinioException e) {
                e.printStackTrace();
                // handle error
                return null;
            }
    }
    
}