package com.pbpoints.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.pbpoints.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.pbpoints.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.pbpoints.domain.User.class.getName());
            createCache(cm, com.pbpoints.domain.Authority.class.getName());
            createCache(cm, com.pbpoints.domain.User.class.getName() + ".authorities");
            createCache(cm, com.pbpoints.domain.Bracket.class.getName());
            createCache(cm, com.pbpoints.domain.Category.class.getName());
            createCache(cm, com.pbpoints.domain.City.class.getName());
            createCache(cm, com.pbpoints.domain.Country.class.getName());
            createCache(cm, com.pbpoints.domain.Country.class.getName() + ".provinces");
            createCache(cm, com.pbpoints.domain.DocType.class.getName());
            createCache(cm, com.pbpoints.domain.Event.class.getName());
            createCache(cm, com.pbpoints.domain.EventCategory.class.getName());
            createCache(cm, com.pbpoints.domain.EventCategory.class.getName() + ".games");
            createCache(cm, com.pbpoints.domain.EventCategory.class.getName() + ".rosters");
            createCache(cm, com.pbpoints.domain.Format.class.getName());
            createCache(cm, com.pbpoints.domain.Formula.class.getName());
            createCache(cm, com.pbpoints.domain.Game.class.getName());
            createCache(cm, com.pbpoints.domain.Location.class.getName());
            createCache(cm, com.pbpoints.domain.Player.class.getName());
            createCache(cm, com.pbpoints.domain.PlayerDetailPoint.class.getName());
            createCache(cm, com.pbpoints.domain.PlayerPoint.class.getName());
            createCache(cm, com.pbpoints.domain.Province.class.getName());
            createCache(cm, com.pbpoints.domain.Province.class.getName() + ".cities");
            createCache(cm, com.pbpoints.domain.Roster.class.getName());
            createCache(cm, com.pbpoints.domain.Roster.class.getName() + ".players");
            createCache(cm, com.pbpoints.domain.Team.class.getName());
            createCache(cm, com.pbpoints.domain.TeamDetailPoint.class.getName());
            createCache(cm, com.pbpoints.domain.TeamPoint.class.getName());
            createCache(cm, com.pbpoints.domain.Tournament.class.getName());
            createCache(cm, com.pbpoints.domain.Tournament.class.getName() + ".events");
            createCache(cm, com.pbpoints.domain.UserExtra.class.getName());
            createCache(cm, com.pbpoints.domain.Field.class.getName());
            createCache(cm, com.pbpoints.domain.Suspension.class.getName());
            createCache(cm, com.pbpoints.domain.MainRoster.class.getName());
            createCache(cm, com.pbpoints.domain.Sponsor.class.getName());
            createCache(cm, com.pbpoints.domain.TournamentGroup.class.getName());
            createCache(cm, com.pbpoints.domain.Equipment.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
