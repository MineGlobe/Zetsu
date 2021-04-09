package me.blazingtide.zetsu.schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.blazingtide.zetsu.adapters.ParameterAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AllArgsConstructor
@Getter
public class CachedTabComplete {
    @Nullable
    final ParameterAdapter<?> parameterAdapter;

    @NotNull
    final List<String> constant;
}
