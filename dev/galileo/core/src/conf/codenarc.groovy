ruleset {
  description 'SeeQuestor RuleSet'

  ruleset('rulesets/basic.xml')
  ruleset('rulesets/exceptions.xml')
  ruleset('rulesets/imports.xml')
  ruleset('rulesets/unused.xml')
  ruleset('rulesets/dry.xml')
  ruleset('rulesets/formatting.xml') {
    'SpaceAroundMapEntryColon' {
      enabled = false
    }
    'ClassJavadoc' {
      enabled = false
    }
    'FileEndsWithoutNewline' {
      enabled = false
    }
  }
  ruleset('rulesets/naming.xml') {
    'MethodName' {
      doNotApplyToClassNames = '*Spec'
    }
    'FactoryMethodName' {
      enabled = false
    }
  }
  ruleset('rulesets/convention.xml')
}
