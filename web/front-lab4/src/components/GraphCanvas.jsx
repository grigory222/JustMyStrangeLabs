import { useEffect, useRef } from "react";
import {canvasToMath, drawGraph} from "../utils/graphUtils";
import PropTypes from "prop-types";
import {useSelector} from "react-redux";

const graphScale = 20;

export function GraphCanvas({ r, formData, formSubmitHandler}) {
    const canvasRef = useRef(null);
    const rows = useSelector(state => state.reducer.results.array);

    useEffect(() => {
        if (canvasRef.current) {
            const canvas = canvasRef.current;
            const ctx = canvas.getContext("2d");
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            console.log('drawing shit...', canvas.width, canvas.height, r)
            drawGraph(canvas, r, rows);
        }
    }, [r, rows]);

    return <canvas
        ref={canvasRef}
        id="myCanvas"
        width={300}
        height={300}
        onClick={async (event) => {
            let {
                x,
                y
            } = canvasToMath(event.clientX, event.clientY, canvasRef.current, formData.r, graphScale * formData.r);
            x = Math.round(x);
            // Ограничение x в диапазоне [-3, 5]
            if (x < -3) x = -3;
            if (x > 5) x = 5;
            console.log("CANVAS LOG: "+x, y, formData.r);
            await formSubmitHandler({x, y, r: formData.r});
        }}
    />;
}

GraphCanvas.propTypes = {
    r: PropTypes.number,
    formData: PropTypes.object,
    formSubmitHandler: PropTypes.func.isRequired,
}