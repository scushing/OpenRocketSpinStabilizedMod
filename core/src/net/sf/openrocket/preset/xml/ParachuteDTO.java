
package net.sf.openrocket.preset.xml;

import net.sf.openrocket.material.Material;
import net.sf.openrocket.preset.ComponentPreset;
import net.sf.openrocket.preset.ComponentPresetFactory;
import net.sf.openrocket.preset.InvalidComponentPresetException;
import net.sf.openrocket.preset.TypedPropertyMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Streamer preset XML handler.
 */
@XmlRootElement(name = "Parachute")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParachuteDTO extends BaseComponentDTO {

    @XmlElement(name = "Diameter")
    private AnnotatedLengthDTO diameter;
    @XmlElement(name = "Sides")
    private Integer sides;
	@XmlElement(name = "PackedDiameter")
	private AnnotatedLengthDTO PackedDiameter;
	@XmlElement(name = "PackedLength")
	private AnnotatedLengthDTO PackedLength;
	@XmlElement(name = "DragCoefficient")
	private AnnotatedLengthDTO dragCoefficient;
    @XmlElement(name = "LineCount")
    private Integer lineCount;
    @XmlElement(name = "LineLength")
    private AnnotatedLengthDTO lineLength;
    
    @XmlElement(name = "LineMaterial")
	private AnnotatedMaterialDTO lineMaterial;
 

    /**
     * Default constructor.
     */
    public ParachuteDTO() {
    }

    public double getDiameter() {
		return diameter.getValue();
	}

	public void setDiameter(AnnotatedLengthDTO diameter) {
		this.diameter = diameter;
	}
	public void setDiameter(double diameter) {
		this.diameter = new AnnotatedLengthDTO(diameter);
	}

	public Integer getSides() {
		return sides;
	}

	public void setSides(Integer sides) {
		this.sides = sides;
	}


	public double getPackedDiameter() {
		return PackedDiameter.getValue();
	}

	public void setPackedDiameter(AnnotatedLengthDTO PackedDiameter) {
		this.PackedDiameter = PackedDiameter;
	}
	public void setPackedDiameter(double PackedDiameter) {
		this.PackedDiameter = new AnnotatedLengthDTO(PackedDiameter);
	}

	public double getPackedLength() {
		return PackedLength.getValue();
	}

	public void setPackedLength(AnnotatedLengthDTO PackedLength) {
		this.PackedLength = PackedLength;
	}
	public void setPackedLength(double PackedLength) {
		this.PackedLength = new AnnotatedLengthDTO(PackedLength);
	}

	public double getDragCoefficient() {
		return dragCoefficient.getValue();
	}

	public void setDragCoefficient(AnnotatedLengthDTO DragCoefficient) {
		this.dragCoefficient = DragCoefficient;
	}
	public void setDragCoefficient(double DragCoefficient) { this.dragCoefficient = new AnnotatedLengthDTO(DragCoefficient); }

	public Integer getLineCount() {
		return lineCount;
	}

	public void setLineCount(Integer lineCount) {
		this.lineCount = lineCount;
	}

	public double getLineLength() {
		return lineLength.getValue();
	}

	public void setLineLength(AnnotatedLengthDTO lineLength) {
		this.lineLength = lineLength;
	}

	public void setLineLength(double lineLength) {
		this.lineLength = new AnnotatedLengthDTO(lineLength);
	}

	public AnnotatedMaterialDTO getLineMaterial() {
		return lineMaterial;
	}

	public void setLineMaterial(AnnotatedMaterialDTO lineMaterial) {
		this.lineMaterial = lineMaterial;
	}

	/**
     * Most-useful constructor that maps a BodyTube preset to a BodyTubeDTO.
     *
     * @param preset  the preset
     *
     * @throws net.sf.openrocket.util.BugException thrown if the expected body tube keys are not in the preset
     */
    public ParachuteDTO(final ComponentPreset preset) {
        super(preset);
        setDiameter(preset.get(ComponentPreset.DIAMETER));
        setLineCount(preset.get(ComponentPreset.LINE_COUNT));
        if ( preset.has(ComponentPreset.LINE_LENGTH)) {
            setLineLength(preset.get(ComponentPreset.LINE_LENGTH));
        }
        if ( preset.has(ComponentPreset.SIDES)) {
        	setSides(preset.get(ComponentPreset.SIDES));
        }
		if ( preset.has(ComponentPreset.PACKED_DIAMETER)) {
			setPackedDiameter(preset.get(ComponentPreset.PACKED_DIAMETER));
		}
		if ( preset.has(ComponentPreset.PACKED_LENGTH)) {
			setPackedLength(preset.get(ComponentPreset.PACKED_LENGTH));
		}
		if ( preset.has(ComponentPreset.PARACHUTE_CD)) {
			setDragCoefficient(preset.get(ComponentPreset.PARACHUTE_CD));
		}
        if ( preset.has(ComponentPreset.LINE_MATERIAL)) {
        	setLineMaterial(new AnnotatedMaterialDTO(preset.get(ComponentPreset.LINE_MATERIAL)));
        }
    }

    @Override
    public ComponentPreset asComponentPreset(Boolean legacy, java.util.List<MaterialDTO> materials) throws InvalidComponentPresetException {
        return asComponentPreset(legacy, ComponentPreset.Type.PARACHUTE, materials);
    }

    public ComponentPreset asComponentPreset(Boolean legacy, ComponentPreset.Type type, List<MaterialDTO> materials) throws InvalidComponentPresetException {
        TypedPropertyMap props = new TypedPropertyMap();
		props.put(ComponentPreset.LEGACY, legacy);
        addProps(props, materials);
        // TODO - seems some vendors use a bulk material for the sheet along with a Thickness.
        // need to fix the MATERIAL packed into the componentpreset.
        props.put(ComponentPreset.TYPE, type);
        props.put(ComponentPreset.DIAMETER, this.getDiameter());
		if ( this.PackedDiameter != null ) {
			props.put(ComponentPreset.PACKED_DIAMETER, this.getPackedDiameter());
		}
		if ( this.PackedLength != null ) {
			props.put(ComponentPreset.PACKED_LENGTH, this.getPackedLength());
		}
		if ( this.dragCoefficient != null ) {
			props.put(ComponentPreset.PARACHUTE_CD, this.getDragCoefficient());
		}
        props.put(ComponentPreset.LINE_COUNT, this.getLineCount());
        if ( this.lineLength != null ) {
        	props.put(ComponentPreset.LINE_LENGTH, this.getLineLength());
        }
        if ( this.sides != null ) {
        	props.put(ComponentPreset.SIDES, this.getSides());
        }
        if ( this.lineMaterial != null ) {
        	Material m = find(materials, this.lineMaterial);
        	if ( m != null ) {
        		props.put(ComponentPreset.LINE_MATERIAL, m);
        	}
        }

        return ComponentPresetFactory.create(props);
    }
}
