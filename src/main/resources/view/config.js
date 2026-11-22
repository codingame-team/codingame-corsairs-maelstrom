import { GraphicEntityModule } from './entity-module/GraphicEntityModule.js';
import { TooltipModule } from './tooltip-module/TooltipModule.js';
import { EndScreenModule } from './endscreen-module/EndScreenModule.js';
import { ToggleModule } from './toggle-module/ToggleModule.js';

export const modules = [
    GraphicEntityModule,
    TooltipModule,
    EndScreenModule,
    ToggleModule
];

export const playerColors = [
    '#ff1d5c', // Player 0: Crimson Corsairs
    '#22a1e4'  // Player 1: Azure Privateers
];

export const gameName = 'CorsairsMaelstrom';

export const options = [
    ToggleModule.defineToggle({
        toggle: 'debugVectors',
        title: 'SHOW CURRENTS & CARGO',
        values: {
            'ON': true,
            'OFF': false
        },
        default: true
    })
];

export const overlayAlpha = 0;
