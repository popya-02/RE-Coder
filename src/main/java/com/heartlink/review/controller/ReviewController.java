package com.heartlink.review.controller;

import com.heartlink.review.model.dto.ReviewDto;
import com.heartlink.review.model.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/photomain")
    public String photoMain() {
        return "/review/review_photo/photo-main";
    }

    @GetMapping("/photoenroll")
    public String photoEnroll() {
        return "/review/review_photo/photo-enroll";
    }

    @GetMapping("/photoedit")
    public String photoEdit() {
        return "/review/review_photo/photo-edit";
    }

    @GetMapping("/photodetail")
    public String photoDetail() {
        return "/review/review_photo/photo-detail";
    }

    @GetMapping("/livemain")
    public String liveMain() {
        return "/review/live-main";
    }

    // photoenroll 페이지에서 글 작성 요청을 처리하는 메서드
    @PostMapping("/submit")
    public String submitPhotoEnroll(@RequestParam("title") String title,
                                    @RequestParam("content") String content,
                                    Model model) {
        try {
            ReviewDto review = new ReviewDto();
            review.setReviewTitle(title);
            review.setReviewContent(content);
            review.setReviewerUserId(7); // 임의의 리뷰어 사용자 ID
            review.setReviewedUserId(8); // 임의의 리뷰 대상 사용자 ID

            boolean isSaved = reviewService.saveReview(review, null);

            if (isSaved) {
                model.addAttribute("message", "글이 성공적으로 작성되었습니다.");
                return "redirect:/review/photomain"; // 글 목록 페이지로 리다이렉트
            } else {
                model.addAttribute("message", "글 작성에 실패했습니다.");
                return "review/review_photo/photo-enroll"; // 글 작성 페이지로 다시 이동
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "오류가 발생했습니다: " + e.getMessage());
            return "review/review_photo/photo-enroll"; // 글 작성 페이지로 다시 이동
        }
    }
}
