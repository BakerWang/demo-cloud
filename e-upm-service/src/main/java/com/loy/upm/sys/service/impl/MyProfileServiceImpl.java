/*
 * Copyright   Loy Fu. 付厚俊
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package com.loy.upm.sys.service.impl;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.loy.e.common.util.Assert;
import com.loy.e.core.annotation.ControllerLogExeTime;
import com.loy.e.core.api.UserBaseService;
import com.loy.upm.sys.domain.entity.UserEntity;
import com.loy.upm.sys.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import com.loy.e.common.annotation.Author;
@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@RestController
@RequestMapping(value = "**/profile", method = { RequestMethod.POST, RequestMethod.GET })
@Transactional
@Api(value = "个人相关信息操作", description = "个人相关信息操作")
public class MyProfileServiceImpl {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserBaseService userBaseService;
    @Autowired
    UserRepository userRepository;

    @ControllerLogExeTime(description = "上传个人图片")
    @RequestMapping(value = "/upload", method = { RequestMethod.POST })

    @ApiOperation(value = "上传个人图片", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "avatar", value = "图片", paramType = "form", required = true, dataType = "file")
    })

    public void upload(@RequestParam MultipartFile avatar) throws IOException {
        byte[] photoData = avatar.getBytes();
        String username = userBaseService.getSessionUser().getUsername();
        UserEntity userEntity = userRepository.findByUsername(username);
        userEntity.setPhotoData(photoData);
        userRepository.save(userEntity);
    }

    @ControllerLogExeTime(description = "获取个人图片", log = false)
    @RequestMapping(value = "/photo", method = { RequestMethod.GET })

    @ApiOperation(value = "获取用户图片", httpMethod = "GET")

    public void photo(HttpServletResponse response) throws IOException {

        String username = userBaseService.getSessionUser().getUsername();
        UserEntity userEntity = userRepository.findByUsername(username);
        byte[] photoData = null;
        if (userEntity != null) {
            photoData = userEntity.getPhotoData();
        }
        OutputStream out = response.getOutputStream();
        if (photoData != null) {
            out.write(photoData);
        }
        out.flush();
        out.close();
    }

    @ControllerLogExeTime(description = "编辑个人资料", log = false)
    @RequestMapping(value = "/edit", method = { RequestMethod.POST })

    @ApiOperation(value = "获取个人信息", httpMethod = "GET")

    public UserEntity get() {
        UserEntity user = userRepository
                .findByUsername(userBaseService.getSessionUser().getUsername());
        user.buildRoleIdAnadName();
        return user;
    }

    @ControllerLogExeTime(description = "修改个人资料")
    @RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.PUT })

    @ApiOperation(value = "修改个人资料", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "email", value = "邮箱", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "phone", value = "电话", paramType = "form", dataType = "string"),
    })

    public void update(@ApiIgnore UserEntity user) {
        String username = userBaseService.getSessionUser().getUsername();
        UserEntity userEntity = userRepository.findByUsername(username);
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPhone(user.getPhone());
        userRepository.save(userEntity);
    }

    @ControllerLogExeTime(description = "修改个人密码")
    @RequestMapping(value = "/password", method = { RequestMethod.POST, RequestMethod.PUT })

    @ApiOperation(value = "修改个人密码", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", paramType = "form", required = true, dataType = "string"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", paramType = "form", required = true, dataType = "string")
    })

    public void updatePassword(String oldPassword, String newPassword) {
        String username = userBaseService.getSessionUser().getUsername();
        UserEntity user = userRepository.findByUsername(username);
        String password = user.getPassword();

        //oldPassword = passwordEncoder.encode(oldPassword);
        if (passwordEncoder.matches(oldPassword, password)) {
            String enPassword = passwordEncoder.encode(newPassword);
            user.setPassword(enPassword);
            userRepository.save(user);
        } else {
            Assert.throwException("sys.user.old_password");
        }

    }

}
