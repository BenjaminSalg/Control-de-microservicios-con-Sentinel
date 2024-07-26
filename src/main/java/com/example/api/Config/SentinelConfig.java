package com.example.api.Config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SentinelConfig {

    @Bean
    public void initSentinel() {
        List<FlowRule> rules = new ArrayList<>();

        //getAllUsuariosAleatorio
        //getAllUsuarios

        /*REJECT*/
        FlowRule fr = new FlowRule();
        fr.setResource("getAllUsuariosAleatorio");
        fr.setGrade(RuleConstant.FLOW_GRADE_QPS);
        fr.setCount(5);
        fr.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        rules.add(fr);

        FlowRuleManager.loadRules(rules);
    }
}

