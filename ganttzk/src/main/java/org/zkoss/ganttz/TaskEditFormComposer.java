/*
 * This file is part of ###PROJECT_NAME###
 *
 * Copyright (C) 2009 Fundación para o Fomento da Calidade Industrial e
 *                    Desenvolvemento Tecnolóxico de Galicia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.zkoss.ganttz;

import java.util.Date;

import org.zkoss.ganttz.data.Task;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.api.Window;

public class TaskEditFormComposer extends GenericForwardComposer {

    public TaskEditFormComposer() {

    }

    private Window window;

    private Task currentTask;
    private TaskDTO taskDTO;

    private Textbox name;

    private Datebox startDateBox;

    private Datebox endDateBox;

    private Textbox notes;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        window = (Window) comp;
    }

    public void showEditFormFor(Component openRelativeTo, Task task) {
        this.currentTask = task;
        this.taskDTO = toDTO(task);
        try {
            window.setMode("modal");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        updateComponentValuesForTask(taskDTO);
    }

    private void updateComponentValuesForTask(
            TaskDTO taskDTO) {
        name.setValue(taskDTO.name);
        startDateBox.setValue(taskDTO.beginDate);
        endDateBox.setValue(taskDTO.endDate);
        notes.setValue(taskDTO.notes);
    }

    public void accept() {
        copyFromDTO(taskDTO, currentTask);
        window.setVisible(false);
    }

    public void cancel() {
        window.setVisible(false);
        currentTask = null;
        taskDTO = null;
    }

    /**
     * DTO to manage edition before changes are accepted.
     *
     * @author Manuel Rego Casasnovas <mrego@igalia.com>
     */
    public class TaskDTO {
        public String name;
        public Date beginDate;
        public Date endDate;
        public String notes;
    }

    private TaskDTO toDTO(Task task) {
        TaskDTO result = new TaskDTO();

        result.name = task.getName();
        result.beginDate = task.getBeginDate();
        result.endDate = new Date(task.getBeginDate().getTime()
                + task.getLengthMilliseconds());
        result.notes = task.getNotes();

        return result;
    }

    private void copyFromDTO(TaskDTO taskDTO, Task currentTask) {
        currentTask.setName(taskDTO.name);
        currentTask.setBeginDate(taskDTO.beginDate);
        currentTask.setLengthMilliseconds(taskDTO.endDate.getTime()
                - taskDTO.beginDate.getTime());
        currentTask.setNotes(taskDTO.notes);
    }

    public TaskDTO getTaskDTO() {
        return this.taskDTO;
    }

}
