package com.app.happybox.repository.subscript;

import com.app.happybox.entity.subscript.Food;
import com.app.happybox.entity.subscript.FoodCalendar;
import com.app.happybox.entity.subscript.Subscription;
import com.app.happybox.entity.subscript.SubscriptionDTO;
import com.app.happybox.repository.user.WelfareRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional @Rollback(false)
@Slf4j
class SubscriptionRepositoryTests {
    @Autowired
    private WelfareRepository welfareRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private FoodCalendarRepository foodCalendarRepository;

    @Test
    public void saveTest() {
        // given
        Subscription subscription = new Subscription(
                "강남 맛집",
                28_000);
        welfareRepository.findById(2L).ifPresent(welfare -> {
            subscription.setWelfare(welfare);
            subscriptionRepository.save(subscription);
        });

        // when

        // then
    }

    @Test
    public void findByIdTest() {
        // given
        subscriptionRepository.findById(3L).map(Subscription::toString).ifPresent(log::info);

        // when

        // then
    }

    @Test
    public void findTop3BetweenDateOrderByDateDesc_QueryDSL_Test() {
        // given

        /* 이번달 시작, 다음달 시작 */
        LocalDateTime currentMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime nextMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atStartOfDay().plusDays(1L);

        List<SubscriptionDTO> subscriptionDTOList =
                subscriptionRepository
                        .findTop3BetweenDateOrderByDateDesc_QueryDSL(
                                currentMonth,
                                nextMonth
                        );

        // when
        subscriptionDTOList.stream().map(SubscriptionDTO::toString).forEach(log::info);
        log.info("이번달 : " + currentMonth.toString());

        // then
    }

    @Test
    public void findTop4OrderByReviewCount_QueryDSL_Test() {
        // given
        subscriptionRepository
                .findTop4OrderByReviewCount_QueryDSL()
                .stream()
                .map(SubscriptionDTO::toString)
                .forEach(log::info);

        // when

        // then
    }

    @Test
    public void findAllByAddressCategoryWithPaging_QueryDSL() {
        // given
        Page<SubscriptionDTO> searchResult = subscriptionRepository
                .findAllByAddressCategoryWithPaging_QueryDSL(PageRequest.of(0, 10), "강남구");

        // when

        // then
        searchResult.get().map(SubscriptionDTO::toString).forEach(log::info);
    }

    @Test
    public void findTop8OrderByDate_QueryDSL() {
        // given
        List<Subscription> subscriptionList = subscriptionRepository.findTop8OrderByDate_QueryDSL();
        List<Long> ids = subscriptionList.stream().map(Subscription::getId).collect(Collectors.toList());

        List<Food> foodList = new ArrayList<>();

        // when
        List<FoodCalendar> foodCalendarList = foodCalendarRepository.findAllInSubscriptionIds(ids);

        // then
        subscriptionList.forEach(subscription -> {
            log.info(subscription.getFoodCalendars().toString());

            foodCalendarList.stream()
                    .filter(foodCalendar -> foodCalendar.getSubscription().equals(subscription))
                    .forEach(foodCalendar -> foodCalendar.getFoodList().forEach(foodList::add));
        });

        log.info(foodList.toString());
    }
//
//    @Test
//    public void findByIdWithReviewCountAndReviewRatingAvgAndOrderCount_QueryDSL(){
//        // given
//        SubscriptionDTO subscriptionDTO =
//                subscriptionRepository.findByIdWithDetail_QueryDSL(3L);
//
//        // when
//
//        // then
//        log.info(subscriptionDTO.toString());
//    }
//
//    @Test
//    public void findFoodCalendar(){
//        // given
//        Optional<Subscription> subscription = subscriptionRepository.findById(3L);
//
//        subscriptionRepository.findFoodCalendar(subscription.get()).stream().map(Food::toString).forEach(log::info);
//
//        // when
//
//        // then
//    }
}