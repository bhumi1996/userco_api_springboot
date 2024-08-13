package com.secui.mvc.utility;

import com.secui.mvc.entity.*;
import com.secui.mvc.response.CountryResponseDto;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
public class FilterSearch {
    public Specification<UserEntity> getUserQuery(String status, String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.STATUS), status));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get(ColumnUtils.EMAIL), "%" + search + "%"),
                                criteriaBuilder.like(root.get(ColumnUtils.FULL_NAME), "%" + search + "%"),
                                criteriaBuilder.like(root.get(ColumnUtils.MOBILE_NUMBER), "%" + search + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<RoleEntity> getRoleQuery(String status, String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.STATUS), status));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get(ColumnUtils.NAME), "%" + search + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<CountryResponseDto> getCountryQuery(String status, String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.STATUS), status));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get(ColumnUtils.CAPITAL), "%" + search + "%"),
                                criteriaBuilder.like(root.get(ColumnUtils.ISOTWO), "%" + search + "%"),
                                criteriaBuilder.like(root.get(ColumnUtils.ISOTHREE), "%" + search + "%"),
                                criteriaBuilder.like(root.get(ColumnUtils.NAME), "%" + search + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<EmailTemplateEntity> getTemplateQuery(String template, String status, String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.STATUS), status));
            }
            if (template != null && !template.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.TEMPLATE_NAME), template));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get(ColumnUtils.TEMPLATE_NAME), "%" + search + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<LeadEntity> getLeadQuery(String stage, String channel, String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (stage != null && !stage.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.STAGE), stage));
            }
            if (channel != null && !channel.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.CHANNEL), channel));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get(ColumnUtils.LEAD_NAME), "%" + search + "%"),
                                criteriaBuilder.like(root.get(ColumnUtils.EMAIL), "%" + search + "%"),
                                criteriaBuilder.like(root.get(ColumnUtils.MOBILE_NUMBER), "%" + search + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<EmailConfigurationEntity> getConfigurationQuery(String status, String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.STATUS), status));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get(ColumnUtils.NAME), "%" + search + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<CampaignEntity> getCampaignQuery(String type, String status, String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.STATUS), status));
            }
            if (type != null && !type.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.TYPE), type));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get(ColumnUtils.NAME), "%" + search + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<GroupEntity> getGroupQuery(String status, String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.STATUS), status));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get(ColumnUtils.GROUP_NAME), "%" + search + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<PortalEntity> getPortalQuery(String status, String search) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.STATUS), status));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get(ColumnUtils.DOMAIN_NAME), "%" + search + "%"),
                                criteriaBuilder.like(root.get(ColumnUtils.SHORT_NAME), "%" + search + "%"),
                                criteriaBuilder.like(root.get(ColumnUtils.PORTAL_NAME), "%" + search + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<ServiceEntity> getServiceQuery(String status, String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.STATUS), status));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get(ColumnUtils.SERVICE_NAME), "%" + search + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<TestimonialEntity> getTestimonialQuery(String status, String search,String portal) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.STATUS), status));
            }
            if (portal != null && !portal.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.PORTAL_NAME), portal));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get(ColumnUtils.NAME), "%" + search + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<CampaignTypeEntity> getTypeQuery(String status, String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.STATUS), status));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get(ColumnUtils.TYPE), "%" + search + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<LeadEntity> addGroupLeadQuery(List<Long> pIds, String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            if (pIds != null && !pIds.isEmpty()) {
                predicates.add(criteriaBuilder.not(criteriaBuilder.in(root.get(ConstantUtil.PID)).value(pIds)));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get(ColumnUtils.EMAIL), "%" + search + "%"),
                        criteriaBuilder.like(root.get(ColumnUtils.NAME), "%" + search + "%"),
                        criteriaBuilder.like(root.get(ColumnUtils.MOBILE_NUMBER), "%" + search + "%")
                ));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<LeadEntity> groupLeadFilterQuery(List<Long> pIds, String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            if (pIds != null) {
                predicates.add(criteriaBuilder.in(root.get(ConstantUtil.PID)).value(pIds));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get(ColumnUtils.EMAIL), "%" + search + "%"),
                        criteriaBuilder.like(root.get(ColumnUtils.NAME), "%" + search + "%"),
                        criteriaBuilder.like(root.get(ColumnUtils.MOBILE_NUMBER), "%" + search + "%")
                ));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<FaqEntity> getFaqQuery(String status, String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.STATUS), status));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get(ColumnUtils.FAQ_QUESTION), "%" + search + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<ArticleEntity> getArticleQuery(String portal, String status, String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.STATUS), status));
            }
            if (portal != null && !portal.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get(ColumnUtils.PORTAL_NAME), portal));
            }
            if (search != null && !search.isEmpty()) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get(ColumnUtils.FAQ_QUESTION), "%" + search + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
