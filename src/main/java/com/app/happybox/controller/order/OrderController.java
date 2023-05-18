package com.app.happybox.controller.order;

import com.app.happybox.domain.AddressDTO;
import com.app.happybox.domain.OrderInfoDTO;
import com.app.happybox.domain.product.ProductCartDTO;
import com.app.happybox.domain.product.ProductDTO;
import com.app.happybox.entity.board.ReviewBoardDTO;
import com.app.happybox.entity.file.ProductFileDTO;
import com.app.happybox.provider.UserDetail;
import com.app.happybox.repository.product.ProductFileRepository;
import com.app.happybox.service.order.ProductOrderService;
import com.app.happybox.service.order.SubscriptionOrderService;
import com.app.happybox.service.product.ProductCartService;
import com.app.happybox.service.product.ProductFileService;
import com.app.happybox.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Qualifier("product")
    private final ProductOrderService productOrderService;
    @Qualifier("subscription")
    private final SubscriptionOrderService subscriptionOrderService;

    private final ProductService productService;
    private final ProductCartService productCartService;
    private final ProductFileService productFileService;

    @GetMapping("product")
    public String goProductOrderForm(@AuthenticationPrincipal UserDetail userDetail, Model model) {
        Long id = userDetail.getId();
        log.error(id.toString(),"오류가난다");
        /* 회원의 전체 장바구니를 조회해옴 */
        List<ProductCartDTO> productCartDTOS = productCartService.findAllByUserId(id);

//        log.info(productCartDTOS.toString());

//      /* 장바구니정보를 담는 DTO안에 상품과 상품사진을 담는다. */
        productCartDTOS.forEach((productCartDTO) -> {
            productCartDTO.setProductDTO(productCartService.findProductById(productCartDTO.getId()));
            ProductDTO productDTO = productService.findById(productCartDTO.getProductDTO().getId());
            Long productId = productDTO.getId();
            ProductFileDTO productFileDTO = productFileService.findFileByProductId(productId);
            productCartDTO.setProductFileDTO(productFileDTO);
        });
        log.error(productCartDTOS.toString(),"에러가난다");
        
        model.addAttribute("productCartDTOS",productCartDTOS);
        model.addAttribute("userDetail",userDetail);
        return "market/payment";
    }

    @PostMapping("product")
    @ResponseBody
    public Long orderProduct(@RequestParam("cartId") List<Long> cartIds, AddressDTO addressDTO, OrderInfoDTO orderInfoDTO, @AuthenticationPrincipal UserDetail userDetail) {
        Long id = userDetail.getId();
        return productOrderService.saveProductOrder(cartIds, id, addressDTO, orderInfoDTO);
    }


    @PostMapping("subscription")
    @ResponseBody
    public Integer register(@RequestParam("cartId") List<Long> cartIds, AddressDTO addressDTO, OrderInfoDTO orderInfoDTO, @AuthenticationPrincipal UserDetail userDetail) {
        Long id = userDetail.getId();
        // 임시 세션 ID 1L
        return subscriptionOrderService.saveSubscriptionOrder(cartIds, id, addressDTO, orderInfoDTO);
    }
}
