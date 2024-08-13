package com.secui.restapi.controller;

import com.secui.mvc.response.TestimonialResponseDto;
import com.secui.mvc.service.TestimonialInterface;
import com.secui.mvc.utility.ConstantUtil;
import com.secui.mvc.utility.UtilHelper;
import com.secui.restapi.annotation.ApiPrefixV1Controller;
import com.secui.restapi.response.TestimonialResponse;
import com.secui.restapi.utility.RestConstantUtil;
import com.secui.restapi.utility.RestUrlConstants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.awt.*;
import java.util.List;
import java.util.Map;


@RequestMapping(RestUrlConstants.API_V1_BASE_URL)
@RequiredArgsConstructor
@ApiPrefixV1Controller
@RestController
public class TestimonialRestController {
private final TestimonialInterface testimonialInterface;


    @RequestMapping(value = RestUrlConstants.TESTIMONIAL, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestimonialResponse>> getTestimonialList(@PathVariable(value = RestConstantUtil.PORTAL) String portal) {
        return testimonialInterface.findAllTestimonial(portal);
    }

@RequestMapping(value = RestUrlConstants.TESTIMONIAL_LIST,method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
 public ResponseEntity<Map<String,Object>> getAllTestimonial(@PathVariable(value = RestConstantUtil.PORTAL) String portal,
                                                             @RequestParam(name = RestConstantUtil.PAGE,required = false,defaultValue = RestUrlConstants.DEFAULT_PAGE_NUMBER) int page,
                                                             @RequestParam(name = RestConstantUtil.SIZE,required = false,defaultValue = RestUrlConstants.DEFAULT_PAGE_SIZE) int size,
                                                             @RequestParam(name = RestConstantUtil.SORT_BY, required = false, defaultValue = StringUtils.EMPTY) String sortBy,
                                                             @RequestParam(name = RestConstantUtil.SORT_DIR, required = false, defaultValue = StringUtils.EMPTY) String sortDir){
    Map<String, String> searchMap = UtilHelper.getSearchMap(null, sortBy, sortDir, null, portal);
    return testimonialInterface.findAll(UtilHelper.pageable(page,size,searchMap.get(RestConstantUtil.SORT_BY),searchMap.get(RestConstantUtil.SORT_DIR)),portal);
}




}
