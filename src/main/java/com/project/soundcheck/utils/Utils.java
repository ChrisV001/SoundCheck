package com.project.soundcheck.utils;

import com.project.soundcheck.dto.ArticleDTO;
import com.project.soundcheck.dto.CarModelDTO;
import com.project.soundcheck.dto.ExhaustSystemDTO;
import com.project.soundcheck.dto.ReviewDTO;
import com.project.soundcheck.model.Article;
import com.project.soundcheck.model.CarModel;
import com.project.soundcheck.model.ExhaustSystem;
import com.project.soundcheck.model.Review;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static ArticleDTO mapArticleToArticleDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();

        articleDTO.setId(article.getId());
        articleDTO.setContent(article.getContent());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setPublishedAt(article.getPublishedAt());
        articleDTO.setCarModelDTO(Utils.mapCarModelToCarModelDTO(article.getCarModel()));

        return articleDTO;
    }

    public static ReviewDTO mapReviewToReviewDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();

        reviewDTO.setId(review.getId());
        reviewDTO.setCreatedAt(review.getCreatedAt());
        reviewDTO.setExhaustSystem(review.getExhaustSystem());

        return reviewDTO;
    }

    public static ExhaustSystemDTO mapExhaustSystemToExhaustSystemDTO(ExhaustSystem exhaustSystem) {
        ExhaustSystemDTO exhaustSystemDTO = new ExhaustSystemDTO();

        exhaustSystemDTO.setId(exhaustSystem.getId());
        exhaustSystemDTO.setName(exhaustSystem.getName());
        exhaustSystemDTO.setReviews(exhaustSystem.getReviews().stream().map(Utils::mapReviewToReviewDTO).collect(Collectors.toList()));
        exhaustSystemDTO.setType(exhaustSystem.getType());
        exhaustSystemDTO.setMaterial(exhaustSystem.getMaterial());
        exhaustSystemDTO.setCarModels(exhaustSystem.getCarModels().stream().map(Utils::mapCarModelToCarModelDTO).collect(Collectors.toSet()));
        exhaustSystemDTO.setPerformanceMetrics(exhaustSystem.getPerformanceMetrics());
        exhaustSystemDTO.setSoundProfile(exhaustSystem.getSoundProfile());

        return exhaustSystemDTO;
    }

    public static CarModelDTO mapCarModelToCarModelDTO(CarModel carModel) {
        CarModelDTO carModelDTO = new CarModelDTO();

        carModelDTO.setId(carModel.getId());
        carModelDTO.setModel(carModel.getModel());
        carModelDTO.setYear(carModel.getYear());
        carModelDTO.setEngineType(carModel.getEngineType());
        carModelDTO.setExhaustSystems(
            carModel.getExhaustSystems().stream().map(Utils::mapExhaustSystemToExhaustSystemDTO).collect(Collectors.toSet())
        );

        return carModelDTO;
    }

    public static List<ArticleDTO> mapArticleListToArticleDTOList(List<Article> articles) {
        return articles.stream().map(Utils::mapArticleToArticleDTO).collect(Collectors.toList());
    }

    public static List<CarModelDTO> mapCarModelListToCarModelDTOList(List<CarModel> carModels) {
        return carModels.stream().map(Utils::mapCarModelToCarModelDTO).collect(Collectors.toList());
    }

    public static List<ExhaustSystemDTO> mapExhaustSystemListToExhaustSystemDTOList(List<ExhaustSystem> exhaustSystems) {
        return exhaustSystems.stream().map(Utils::mapExhaustSystemToExhaustSystemDTO).collect(Collectors.toList());
    }
}
