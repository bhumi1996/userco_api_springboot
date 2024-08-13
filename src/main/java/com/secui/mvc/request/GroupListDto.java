package com.secui.mvc.request;

import com.secui.mvc.utility.ConstantUtil;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupListDto {
    @NotNull(message= ConstantUtil.REQUIRED)
    List<String> groupList=new ArrayList<>();
}
