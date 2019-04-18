package io.github.semlink.semlink;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.semlink.app.DefaultChunking;
import io.github.semlink.app.Span;
import io.github.semlink.parser.Proposition;
import io.github.semlink.propbank.frames.PbRole;
import io.github.semlink.propbank.type.PropBankArg;
import io.github.semlink.verbnet.type.ThematicRoleType;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * Semlink role, providing a unified view of corresponding PropBank and VerbNet roles.
 *
 * @author jgung
 */
@Data
@Accessors(fluent = true)
public class SemlinkRole {

    private PropBankArg propBankArg;
    private ThematicRoleType thematicRoleType;
    private PbRole pbRole;

    /**
     * Optionally return the {@link PropBankArg} PropBank argument instance.
     */
    public Optional<PropBankArg> pb() {
        return Optional.ofNullable(propBankArg);
    }

    /**
     * Optionally return the {@link ThematicRoleType VerbNet thematic role}.
     */
    public Optional<ThematicRoleType> vn() {
        return Optional.ofNullable(thematicRoleType);
    }

    /**
     * Optionally return a {@link PbRole PropBank role definition}.
     */
    public Optional<PbRole> definition() {
        return Optional.ofNullable(pbRole);
    }

    public static SemlinkRole of(@NonNull PropBankArg propBankArg) {
        return new SemlinkRole().propBankArg(propBankArg);
    }

    public static <R> Proposition<R, SemlinkRole> convert(@NonNull Proposition<R, PropBankArg> proposition) {
        List<Span<SemlinkRole>> roleSpans = proposition.arguments().spans().stream()
                .map(span -> Span.convert(span, of(span.label())))
                .collect(Collectors.toList());
        return new Proposition<>(proposition.predicate(), new DefaultChunking<>(roleSpans));
    }

    @Override
    public String toString() {
        List<String> parts = new ArrayList<>();
        pb().ifPresent(pb -> parts.add(pb.toString()));
        vn().ifPresent(vn -> parts.add(vn.toString()));
        definition().ifPresent(def -> parts.add(def.description()));
        return String.join(" <-> ", parts);
    }
}
