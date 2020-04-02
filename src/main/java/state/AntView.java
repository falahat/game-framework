package state;

import feature.FeatureExtractor;

import java.util.Collection;

public class AntView implements BoardView {
    @Override
    public Collection<FeatureExtractor<Board, ?>> getRequiredExtractors() {
        // Nearest state.Food Distance
        // Nearest state.Food state.Direction
        //
        return null;
    }
}
