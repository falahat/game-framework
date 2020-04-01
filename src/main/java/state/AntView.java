package state;

import feature.FeatureExtractor;
import state.Board2D;
import state.Board2DView;

import java.util.Collection;

public class AntView implements Board2DView {
    @Override
    public Collection<FeatureExtractor<Board2D, ?>> getRequiredExtractors() {
        // Nearest state.Food Distance
        // Nearest state.Food state.Direction
        //
        return null;
    }
}
