import datadog.trace.api.GlobalTracer;
import datadog.trace.api.interceptor.MutableSpan;
import datadog.trace.api.interceptor.TraceInterceptor;
import datadog.trace.api.sampling.PrioritySampling;

import java.util.Collection;

public class Library {
  public boolean runApp() {
    GlobalTracer.get().addTraceInterceptor(new CustomInterceptor());

    return true;
  }

  public static class CustomInterceptor implements TraceInterceptor {
    @Override
    public Collection<? extends MutableSpan> onTraceComplete(Collection<? extends MutableSpan> trace) {
      // sample based on the root span
      final MutableSpan rootSpan = trace.iterator().next().getRootSpan();
      if (rootSpan.getDurationNano() > 12345 /* your-threshold */) {
        rootSpan.setSamplingPriority(PrioritySampling.USER_DROP);
      }

      // or iterate all spans in a trace
      for (MutableSpan span : trace) {
        if (false /* your sampling logic here*/) {
          span.setSamplingPriority(PrioritySampling.USER_DROP);
        }
      }
      return null;
    }

    @Override
    public int priority() {
      return 0;
    }
  }
}
