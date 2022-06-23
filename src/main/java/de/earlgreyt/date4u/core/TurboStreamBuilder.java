package de.earlgreyt.date4u.core;

import java.util.Map;
import java.util.StringJoiner;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class TurboStreamBuilder {
  private final TemplateEngine templateEngine;

  public TurboStreamBuilder(TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }
  public String buildTurboStream(String action, String target, String templateHTML ,Map<String, Object> values){
    Context context = new Context();
    context.setVariables(values);
    String targetString = templateEngine.process(templateHTML,context).trim();
    targetString ="<turbo-stream target=\""+target+"\" action=\""+action+"\"><template>"+targetString+"</template></turbo-stream>".trim();
    return  targetString.replace("\n","").replace("\r","").trim();
  }

  public String buildRemoveChildTurboStream(String[] targetSelectors) {
    StringJoiner targets = new StringJoiner(">");
    for (String targetSelector : targetSelectors) {
      targets.add(targetSelector);
    }
    String removeStream = "<turbo-stream targets=\""+targets.toString()+"\" action=\"remove\"></turbo-stream>".trim();
    return removeStream;
  }
}
