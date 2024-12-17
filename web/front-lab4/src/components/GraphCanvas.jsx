import { useEffect, useRef } from "react";
import {canvasToMath, drawGraph} from "../utils/graphUtils";
import PropTypes from "prop-types";

const graphScale = 20;

export function GraphCanvas({ r, formData, setFormData, formSubmitHandler, setXAutocomplete}) {
    const canvasRef = useRef(null);

    useEffect(() => {
        if (canvasRef.current) {
            const canvas = canvasRef.current;
            const ctx = canvas.getContext("2d");
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            drawGraph(canvas, r);
        }
    }, [r]);

    return <canvas
        ref={canvasRef}
        id="myCanvas"
        width={400}
        height={400}
        onClick={(event)=>{
            setFormData((prev) => ({ ...prev, x: x, y: y }));

            let {x, y} = canvasToMath(event.clientX, event.clientY, canvasRef.current, formData.r, graphScale*formData.r);
            x = Math.round(x);
            // Ограничение x в диапазоне [-3, 5]
            if (x < -3) x = -3;
            if (x > 5) x = 5;

            setXAutocomplete(x); // Обновляем значение в Autocomplete
            formSubmitHandler(event);
        }}
    />;
}

GraphCanvas.propTypes = {
    r: PropTypes.number,
    formData: PropTypes.object,
    setFormData: PropTypes.func,
    formSubmitHandler: PropTypes.func,
    setXAutocomplete: PropTypes.func,
}