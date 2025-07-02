package com.project.soundcheck.utils;

import com.project.soundcheck.dto.*;
import com.project.soundcheck.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {

    public static ArticleDTO mapArticleToArticleDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();

        articleDTO.setId(article.getId());
        articleDTO.setContent(article.getContent());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setPublishedAt(article.getPublishedAt());
        articleDTO.setCarModelDTO(Utils.mapCarModelToShallowDTO(article.getCarModel()));

        return articleDTO;
    }

    public static CarModelDTO mapCarModelToShallowDTO(CarModel cm) {
        CarModelDTO carModelDTO = new CarModelDTO();

        carModelDTO.setId(cm.getId());
        carModelDTO.setModel(cm.getModel());
        carModelDTO.setYear(cm.getYear());
        carModelDTO.setEngineType(cm.getEngineType());

        return carModelDTO;
    }

    public static ExhaustSystemDTO mapExhaustSystemToShallowDTO(ExhaustSystem es) {
        ExhaustSystemDTO exhaustSystemDTO = new ExhaustSystemDTO();
        exhaustSystemDTO.setId(es.getId());
        exhaustSystemDTO.setName(es.getName());
        exhaustSystemDTO.setType(es.getType());
        exhaustSystemDTO.setReviews(es.getReviews().stream().map(Utils::mapReviewToReviewDTO).collect(Collectors.toList()));
        exhaustSystemDTO.setMaterial(es.getMaterial());
        exhaustSystemDTO.setPerformanceMetrics(es.getPerformanceMetrics());
        exhaustSystemDTO.setSoundProfile(es.getSoundProfile());

        return exhaustSystemDTO;
    }

    public static Article mapArticleDTOtoArticle(ArticleDTO articleDTO) {
        Article article = new Article();

        article.setId(articleDTO.getId());
        article.setContent(articleDTO.getContent());
        article.setTitle(articleDTO.getTitle());
        article.setPublishedAt(articleDTO.getPublishedAt());
        article.setCarModel(Utils.mapCarModelDTOToCarModel(articleDTO.getCarModelDTO()));

        return article;
    }

    public static ReviewDTO mapReviewToReviewDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();

        reviewDTO.setId(review.getId());
        reviewDTO.setCreatedAt(review.getCreatedAt());
        reviewDTO.setExhaustSystem(Utils.mapExhaustSystemToShallowDTO(review.getExhaustSystem()));
        reviewDTO.setUserDTO(Utils.mapUserToUserDTO(review.getUser()));

        return reviewDTO;
    }

    public static Review mapReviewDTOToReview(ReviewDTO reviewDTO) {
        Review review = new Review();

        review.setId(reviewDTO.getId());
        review.setCreatedAt(reviewDTO.getCreatedAt());
        review.setExhaustSystem(Utils.mapExhaustSystemDTOToExhaustSystem(reviewDTO.getExhaustSystem()));
        review.setUser(Utils.mapUserDTOToUser(reviewDTO.getUserDTO()));

        return review;
    }

    public static UserDTO mapUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setReviews(user.getReviews() != null ? user.getReviews().stream().map(Utils::mapReviewToReviewDTO).collect(Collectors.toSet()) : Collections.emptySet());

        return userDTO;
    }

    public static User mapUserDTOToUser(UserDTO userDTO) {
        User user = new User();

        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setUpdatedAt(userDTO.getUpdatedAt());
        user.setReviews(userDTO.getReviews() != null ? userDTO.getReviews().stream().map(Utils::mapReviewDTOToReview).collect(Collectors.toSet()) : Collections.emptySet());

        return user;
    }

    public static ExhaustSystemDTO mapExhaustSystemToExhaustSystemDTO(ExhaustSystem exhaustSystem) {
        ExhaustSystemDTO exhaustSystemDTO = new ExhaustSystemDTO();

        exhaustSystemDTO.setId(exhaustSystem.getId());
        exhaustSystemDTO.setName(exhaustSystem.getName());
        exhaustSystemDTO.setReviews(exhaustSystem.getReviews() != null ? exhaustSystem.getReviews().stream().map(Utils::mapReviewToReviewDTO).collect(Collectors.toList()) : Collections.emptyList());
        exhaustSystemDTO.setType(exhaustSystem.getType());
        exhaustSystemDTO.setMaterial(exhaustSystem.getMaterial());
        exhaustSystemDTO.setCarModels(exhaustSystem.getCarModels().stream().map(Utils::mapCarModelToShallowDTO).collect(Collectors.toSet()));
        exhaustSystemDTO.setPerformanceMetrics(exhaustSystem.getPerformanceMetrics());
        exhaustSystemDTO.setSoundProfile(exhaustSystem.getSoundProfile());

        return exhaustSystemDTO;
    }

    public static ExhaustSystem mapExhaustSystemDTOToExhaustSystem(ExhaustSystemDTO exhaustSystemDTO) {
        ExhaustSystem exhaustSystem = new ExhaustSystem();

        exhaustSystem.setId(exhaustSystemDTO.getId());
        exhaustSystem.setName(exhaustSystemDTO.getName());
        exhaustSystem.setReviews(exhaustSystemDTO.getReviews() != null ? exhaustSystemDTO.getReviews().stream().map(Utils::mapReviewDTOToReview).collect(Collectors.toList()) : Collections.emptyList());
        exhaustSystem.setType(exhaustSystemDTO.getType());
        exhaustSystem.setMaterial(exhaustSystemDTO.getMaterial());
        exhaustSystem.setCarModels(exhaustSystemDTO.getCarModels().stream().map(Utils::mapCarModelDTOToCarModel).collect(Collectors.toSet()));
        exhaustSystem.setPerformanceMetrics(exhaustSystemDTO.getPerformanceMetrics());
        exhaustSystem.setSoundProfile(exhaustSystemDTO.getSoundProfile());

        return exhaustSystem;
    }

    public static CarModelDTO mapCarModelToCarModelDTO(CarModel carModel) {
        CarModelDTO carModelDTO = new CarModelDTO();

        carModelDTO.setId(carModel.getId());
        carModelDTO.setModel(carModel.getModel());
        carModelDTO.setYear(carModel.getYear());
        carModelDTO.setEngineType(carModel.getEngineType());
        carModelDTO.setExhaustSystems(carModel.getExhaustSystems() != null ? carModel.getExhaustSystems().stream().map(Utils::mapExhaustSystemToExhaustSystemDTO).collect(Collectors.toSet()) : Collections.emptySet());

        return carModelDTO;
    }

    public static CarModel mapCarModelDTOToCarModel(CarModelDTO carModelDTO) {
        CarModel carModel = new CarModel();

        carModel.setId(carModelDTO.getId());
        carModel.setModel(carModelDTO.getModel());
        carModel.setYear(carModelDTO.getYear());
        carModel.setEngineType(carModelDTO.getEngineType());
        carModel.setExhaustSystems(carModelDTO.getExhaustSystems() != null ? carModelDTO.getExhaustSystems().stream().map(Utils::mapExhaustSystemDTOToExhaustSystem).collect(Collectors.toSet()) : Collections.emptySet());

        return carModel;
    }

    public static List<ArticleDTO> mapArticleListToArticleDTOList(List<Article> articles) {
        if (articles == null)
            return Collections.emptyList();
        return articles.stream().map(Utils::mapArticleToArticleDTO).collect(Collectors.toList());
    }

    public static List<CarModelDTO> mapCarModelListToCarModelDTOList(List<CarModel> carModels) {
        if (carModels == null)
            return Collections.emptyList();
        return carModels.stream().map(Utils::mapCarModelToCarModelDTO).collect(Collectors.toList());
    }

    public static Set<ExhaustSystemDTO> mapExhaustSystemListToExhaustSystemDTOList(Set<ExhaustSystem> exhaustSystems) {
        if (exhaustSystems == null)
            return Collections.emptySet();
        return exhaustSystems.stream().map(Utils::mapExhaustSystemToExhaustSystemDTO).collect(Collectors.toSet());
    }

    public static Set<ExhaustSystem> mapExhaustSystemDTOSetToExhaustSystemSet(Set<ExhaustSystemDTO> exhaustSystemDTOS) {
        if (exhaustSystemDTOS == null)
            return Collections.emptySet();
        return exhaustSystemDTOS.stream().map(Utils::mapExhaustSystemDTOToExhaustSystem).collect(Collectors.toSet());
    }

    public static List<ReviewDTO> mapReviewListToReviewDTOList(List<Review> reviews) {
        if (reviews == null)
            return Collections.emptyList();
        return reviews.stream().map(Utils::mapReviewToReviewDTO).collect(Collectors.toList());
    }

    public static List<UserDTO> mapUserListToUserDTOList(List<User> users) {
        if (users == null)
            return Collections.emptyList();
        return users.stream().map(Utils::mapUserToUserDTO).collect(Collectors.toList());
    }

    public static Set<CarModel> mapCarModelSetToCarModelDTOSet(Set<CarModelDTO> carModelDTOS) {
        if (carModelDTOS == null)
            return Collections.emptySet();

        return carModelDTOS.stream().map(Utils::mapCarModelDTOToCarModel).collect(Collectors.toSet());
    }

    public static List<Review> mapReviewDTOListToReviewList(List<ReviewDTO> reviewDTOS) {
        if (reviewDTOS == null)
            return Collections.emptyList();
        return reviewDTOS.stream().map(Utils::mapReviewDTOToReview).collect(Collectors.toList());
    }
}
