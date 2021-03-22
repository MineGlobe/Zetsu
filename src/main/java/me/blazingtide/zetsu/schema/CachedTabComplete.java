package me.blazingtide.zetsu.schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.blazingtide.zetsu.adapters.ParameterAdapter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CachedTabComplete {
    final ParameterAdapter<?> parameterAdapter;
    final List<String> constant;
}
