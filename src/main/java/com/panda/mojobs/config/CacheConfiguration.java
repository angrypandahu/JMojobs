package com.panda.mojobs.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache("users", jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.JobSubType.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.Province.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.City.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.Town.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.Address.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.Mjob.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.School.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.School.class.getName() + ".jobs", jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.MojobImage.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.Resume.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.Resume.class.getName() + ".experiencies", jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.Resume.class.getName() + ".educationBackgrounds", jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.Resume.class.getName() + ".professionalDevelopments", jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.Resume.class.getName() + ".languages", jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.BasicInformation.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.Experience.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.EducationBackground.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.ProfessionalDevelopment.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.MLanguage.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.Invitation.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.ApplyJobResume.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.ChatMessage.class.getName(), jcacheConfiguration);
            cm.createCache(com.panda.mojobs.domain.SchoolAddress.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
